/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.request;

import androidx.annotation.NonNull;

import com.gsma.sgp.messages.rspdefinitions.HandleNotification;
import com.gsma.sgp.messages.rspdefinitions.PendingNotification;
import com.infineon.esim.messages.Ber;

public class HandleNotificationReq {
    private String pendingNotification;

    public String getPendingNotification() {
        return pendingNotification;
    }

    public void setPendingNotification(String pendingNotification) {
        this.pendingNotification = pendingNotification;
    }

    public HandleNotification getRequest() {
        HandleNotification handleNotification = new HandleNotification();
        handleNotification.setPendingNotification(getPendingNotificationParsed());

        return handleNotification;
    }

    public void setRequest(HandleNotification handleNotification) {
        setPendingNotificationParsed(handleNotification.getPendingNotification());
    }

    public void setRequest(PendingNotification pendingNotification) {
        setPendingNotificationParsed(pendingNotification);
    }

    private PendingNotification getPendingNotificationParsed() {
        return Ber.createFromEncodedBase64String(PendingNotification.class, pendingNotification);
    }

    private void setPendingNotificationParsed(PendingNotification pendingNotificationParsed) {
        pendingNotification = Ber.getEncodedAsBase64String(pendingNotificationParsed);
    }

    @NonNull
    @Override
    public String toString() {
        return "HandleNotificationReq{" +
                "pendingNotification='" + pendingNotification + '\'' +
                '}';
    }
}
