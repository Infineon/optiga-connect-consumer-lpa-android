/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa.task;

import com.infineon.esim.lpa.core.dtos.result.remote.CancelSessionResult;
import com.infineon.esim.lpa.lpa.LocalProfileAssistant;
import com.infineon.esim.util.Log;

import java.util.concurrent.Callable;

public class CancelSessionTask implements Callable<CancelSessionResult> {
    private static final String TAG = CancelSessionTask.class.getName();

    private final LocalProfileAssistant lpa;
    private final long cancelSessionReason;

    public CancelSessionTask(LocalProfileAssistant lpa, long cancelSessionReason) {
        this.lpa = lpa;
        this.cancelSessionReason = cancelSessionReason;
    }

    @Override
    public CancelSessionResult call() throws Exception {
        try {
            return lpa.cancelSession(cancelSessionReason);
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(TAG,"CancelSession failed with exception: " + e.getMessage());
            return new CancelSessionResult(lpa.getLastError());
        }
    }
}
