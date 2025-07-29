/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.remote;

import com.beanit.jasn1.ber.types.string.BerUTF8String;
import com.gsma.sgp.messages.rspdefinitions.ListNotificationResponse;
import com.gsma.sgp.messages.rspdefinitions.NotificationMetadata;
import com.gsma.sgp.messages.rspdefinitions.NotificationSentResponse;
import com.gsma.sgp.messages.rspdefinitions.PendingNotification;
import com.gsma.sgp.messages.rspdefinitions.RetrieveNotificationsListResponse;
import com.infineon.esim.lpa.core.dtos.result.local.NotificationSentResult;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.lpa.core.es10.definitions.NotificationEvents;
import com.infineon.esim.lpa.core.es9plus.Es9PlusInterface;
import com.infineon.esim.util.Log;

import java.util.List;

public class HandleNotificationsWorker {
    private static final String TAG = HandleNotificationsWorker.class.getName();

    private final Es10Interface es10Interface;
    private final Es9PlusInterface es9PlusInterface;

    public HandleNotificationsWorker(Es10Interface es10Interface, Es9PlusInterface es9PlusInterface) {
        this.es10Interface = es10Interface;
        this.es9PlusInterface = es9PlusInterface;
    }

    public boolean handleNotifications() throws Exception {
        Log.debug(TAG, "Handling notifications...");

        // ES10: Get list of notifications from eUICC
        ListNotificationResponse listNotificationResponse = es10Interface.es10b_listNotification(NotificationEvents.ALL);

        if(listNotificationResponse.getNotificationMetadataList() == null
        || listNotificationResponse.getNotificationMetadataList().getNotificationMetadata() == null) {
            Log.error(TAG, "Error: NotificationMetadataList is null.");
            return false;
        }

        List<NotificationMetadata> notificationMetadataList = listNotificationResponse.getNotificationMetadataList().getNotificationMetadata();

        // Do presorting of notifications according to notificationAddress
        notificationMetadataList.sort((notificationMetadata1, notificationMetadata2) -> {
            String notificationAddress1 = notificationMetadata1.getNotificationAddress().toString();
            String notificationAddress2 = notificationMetadata2.getNotificationAddress().toString();
            return notificationAddress1.compareTo(notificationAddress2);
        });

        boolean success = true;
        for (NotificationMetadata notification : listNotificationResponse.getNotificationMetadataList().getNotificationMetadata()) {
            Log.debug(TAG, "Notification: " + notification);

            // ES10: Retrieve full notification details from eUICC
            RetrieveNotificationsListResponse retrieveNotificationsListResponse = es10Interface.es10b_retrieveNotificationsListBySeqNumber(notification.getSeqNumber());

            // Should be only one pending notification
            if(retrieveNotificationsListResponse.getNotificationList() == null) {
                Log.error(TAG, "Error: NotificationList is null.");
                return false;
            }

            for (PendingNotification pendingNotification : retrieveNotificationsListResponse.getNotificationList().getPendingNotification()) {
                // ES9+: Send notification to SM-DP+
                BerUTF8String notificationAddress = notification.getNotificationAddress();
                es9PlusInterface.setSmdpAddress(notificationAddress.toString());
                es9PlusInterface.handleNotification(pendingNotification);

                // ES10: Remove notification
                NotificationSentResponse notificationSentResponse = es10Interface.es10b_removeNotificationFromList(notification.getSeqNumber());
                NotificationSentResult notificationSentResult = new NotificationSentResult(notificationSentResponse.getDeleteNotificationStatus().intValue());

                if(!notificationSentResult.isOk()) {
                    success = false;
                }
            }
        }

        return success;
    }
}
