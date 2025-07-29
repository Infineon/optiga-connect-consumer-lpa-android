/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa.task;

import com.infineon.esim.lpa.core.dtos.result.remote.DownloadResult;
import com.infineon.esim.lpa.core.dtos.result.remote.HandleNotificationsResult;
import com.infineon.esim.lpa.lpa.LocalProfileAssistant;
import com.infineon.esim.util.Log;

import java.net.ConnectException;
import java.util.concurrent.Callable;

public class DownloadTask implements Callable<DownloadResult> {
    private static final String TAG = DownloadTask.class.getName();

    private final LocalProfileAssistant lpa;
    private final String confirmationCode;

    public DownloadTask(LocalProfileAssistant lpa, String confirmationCode) {
        this.lpa = lpa;
        this.confirmationCode = confirmationCode;
    }

    @Override
    public DownloadResult call() {
        try {
            // Download the profile
            DownloadResult downloadResult = lpa.downloadProfile(confirmationCode);

            // Send notification
            HandleNotificationsResult handleNotificationsResult;
            try {
                handleNotificationsResult = lpa.handleNotifications();
            } catch (ConnectException e) {
                // Ignore exceptions (E.g. no internet connection) and retry later
                return downloadResult;
            }

            if(handleNotificationsResult.getSuccess()) {
                return downloadResult;
            } else {
                return new DownloadResult(handleNotificationsResult.getRemoteError());
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(TAG," " + "Downloading profile failed with exception: " + e.getMessage());
            return new DownloadResult(lpa.getLastError());
        }
    }
}
