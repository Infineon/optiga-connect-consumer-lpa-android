/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa.task;

import com.infineon.esim.lpa.core.dtos.ActivationCode;
import com.infineon.esim.lpa.core.dtos.result.remote.AuthenticateResult;
import com.infineon.esim.lpa.lpa.LocalProfileAssistant;
import com.infineon.esim.util.Log;

import java.util.concurrent.Callable;

public class AuthenticateTask implements Callable<AuthenticateResult> {
    private static final String TAG = AuthenticateTask.class.getName();

    private final LocalProfileAssistant lpa;
    private final ActivationCode activationCode;

    public AuthenticateTask(LocalProfileAssistant lpa, ActivationCode activationCode) {
        this.lpa = lpa;
        this.activationCode = activationCode;
    }

    @Override
    public AuthenticateResult call() throws Exception {

        try {
            return lpa.authenticate(activationCode);
        } catch (Exception e) {
            Log.error(TAG," " + "Authenticating failed with exception: " + e.getMessage());
            throw e;
        }
    }
}
