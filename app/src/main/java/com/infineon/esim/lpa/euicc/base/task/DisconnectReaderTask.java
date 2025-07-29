/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base.task;

import com.infineon.esim.lpa.euicc.base.EuiccInterface;
import com.infineon.esim.util.Log;

import java.util.concurrent.Callable;

public class DisconnectReaderTask implements Callable<Boolean> {
    private static final String TAG = DisconnectReaderTask.class.getName();

    private final EuiccInterface euiccInterface;

    public DisconnectReaderTask(EuiccInterface euiccInterface) {
        this.euiccInterface = euiccInterface;
    }

    @Override
    public Boolean call() throws Exception {
        try {
            return euiccInterface.disconnectInterface();
//            return readerManager.disconnectReader(readerTag);
        } catch (Exception e) {
            Log.error(TAG,"Exception during disconnecting interface " + euiccInterface.getTag() + " .");
            throw new Exception("Exception during disconnecting interface" + euiccInterface.getTag() + " .", e);
        }
    }
}
