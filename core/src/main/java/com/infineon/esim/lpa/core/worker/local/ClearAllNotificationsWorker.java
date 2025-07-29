/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.local;

import com.gsma.sgp.messages.rspdefinitions.ListNotificationResponse;
import com.gsma.sgp.messages.rspdefinitions.NotificationMetadata;
import com.gsma.sgp.messages.rspdefinitions.NotificationSentResponse;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.lpa.core.es10.definitions.NotificationEvents;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ClearAllNotificationsWorker {
    private static final String TAG = ClearAllNotificationsWorker.class.getName();

    private final Es10Interface es10Interface;

    public ClearAllNotificationsWorker(Es10Interface es10Interface) {
        this.es10Interface = es10Interface;
    }

    public List<Integer> clearAllNotifications() throws Exception {
        Log.debug(TAG, "Clearing all pending notifications...");

        List<Integer> returnValues = new ArrayList<>();

        // Get all pending notifications as list
        ListNotificationResponse listNotificationResponse = es10Interface.es10b_listNotification(NotificationEvents.ALL);

        // Remove notifications one by one
        for (NotificationMetadata notification : listNotificationResponse.getNotificationMetadataList().getNotificationMetadata()) {
            String seqNo = Bytes.encodeHexString(notification.getSeqNumber().value.toByteArray());

            Log.debug(TAG,"Removing notification with seqNo " + seqNo);

            NotificationSentResponse notificationSentResponse = es10Interface.es10b_removeNotificationFromList(notification.getSeqNumber());

            Log.debug(TAG,"Remove notification response for seqNo " + seqNo + ": " + notificationSentResponse);

            returnValues.add(notificationSentResponse.getDeleteNotificationStatus().intValue());
        }

        return returnValues;
    }
}
