/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base.task;

import com.infineon.esim.lpa.euicc.base.EuiccInterface;
import com.infineon.esim.util.Log;

import java.util.concurrent.Callable;

public class ConnectInterfaceTask implements Callable<Void> {
    private static final String TAG = ConnectInterfaceTask.class.getName();

    private final EuiccInterface euiccInterface;

    public ConnectInterfaceTask(EuiccInterface euiccInterface) {
        this.euiccInterface = euiccInterface;
    }

    @Override
    public Void call() throws Exception {
        try {
            boolean result = euiccInterface.connectInterface();
            Log.debug(TAG, "Connecting interface " + euiccInterface.getTag() + " result: " + result);

        } catch (Exception e) {
            Log.error(TAG,"Exception during connecting " + euiccInterface.getTag() + " interface.");
            throw new Exception("Exception during connecting " + euiccInterface.getTag() + " interface.", e);
        }
        return null;
    }
}
