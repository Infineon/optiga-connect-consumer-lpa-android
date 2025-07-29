/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa.task;

import com.infineon.esim.lpa.core.dtos.EuiccInfo;
import com.infineon.esim.lpa.lpa.LocalProfileAssistant;
import com.infineon.esim.util.Log;

import java.util.concurrent.Callable;

public class GetEuiccInfoTask implements Callable<EuiccInfo> {
    private static final String TAG = GetEuiccInfoTask.class.getName();

    private final LocalProfileAssistant lpa;

    public GetEuiccInfoTask(LocalProfileAssistant lpa) {
        this.lpa = lpa;
    }

    @Override
    public EuiccInfo call() throws Exception {
        try {
            String eid = lpa.getEID();
            Log.debug(TAG,"EID: " + eid);

            EuiccInfo euiccInfo = lpa.getEuiccInfo2();
            euiccInfo.setEid(eid);

            Log.debug(TAG,"EuiccInfo: " + euiccInfo);

            return euiccInfo;
        } catch (Exception e) {
            e.printStackTrace();
            Log.error(TAG, "Getting eUICC info failed with exception: " + e.getMessage());
            throw e;
        }
    }
}
