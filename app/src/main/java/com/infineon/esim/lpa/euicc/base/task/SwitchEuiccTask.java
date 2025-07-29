/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base.task;

import com.infineon.esim.lpa.euicc.EuiccConnectionSettings;
import com.infineon.esim.lpa.euicc.base.EuiccConnection;
import com.infineon.esim.util.Log;

import java.util.concurrent.Callable;

public class SwitchEuiccTask implements Callable<Boolean> {
    private static final String TAG = SwitchEuiccTask.class.getName();

    private final EuiccConnection oldEuiccConnection;
    private final EuiccConnection newEuiccConnection;
    private final EuiccConnectionSettings euiccConnectionSettings;

    public SwitchEuiccTask(EuiccConnection oldEuiccConnection, EuiccConnection newEuiccConnection, EuiccConnectionSettings euiccConnectionSettings) {
        this.oldEuiccConnection = oldEuiccConnection;
        this.newEuiccConnection = newEuiccConnection;
        this.euiccConnectionSettings = euiccConnectionSettings;
    }

    @Override
    public Boolean call() throws Exception {
        Log.debug(TAG,"Switching eUICC reader...");

        // Close the active eUICC connection
        if(oldEuiccConnection != null) {
            Log.debug(TAG, "Closing old eUICC connection first...");
            oldEuiccConnection.close();
        }

        // Update eUICC connection settings for new reader
        Log.debug(TAG, "Updating eUICC connection settings...");
        newEuiccConnection.updateEuiccConnectionSettings(euiccConnectionSettings);

        // Open new eUICC connection
        Log.debug(TAG, "Opening new eUICC connection ...");
        return newEuiccConnection.open();
    }
}
