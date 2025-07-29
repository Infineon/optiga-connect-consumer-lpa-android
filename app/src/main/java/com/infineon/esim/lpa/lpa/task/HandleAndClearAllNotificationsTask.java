/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa.task;

import com.infineon.esim.lpa.core.dtos.result.local.ClearNotificationsResult;
import com.infineon.esim.lpa.lpa.LocalProfileAssistant;

import java.util.concurrent.Callable;

public class HandleAndClearAllNotificationsTask implements Callable<Boolean> {
    private final LocalProfileAssistant lpa;

    public HandleAndClearAllNotificationsTask(LocalProfileAssistant lpa) {
        this.lpa = lpa;
    }

    @Override
    public Boolean call() throws Exception {
        // Clear the pending notifications
        ClearNotificationsResult clearNotificationsResult = lpa.clearPendingNotifications();

        return clearNotificationsResult.isOk();
    }
}