/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.local;

import com.gsma.sgp.messages.rspdefinitions.GetEuiccDataResponse;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.util.Log;

public class GetEidWorker {
    private static final String TAG = GetEidWorker.class.getName();

    private final Es10Interface es10Interface;

    public GetEidWorker(Es10Interface es10Interface) {
        this.es10Interface = es10Interface;
    }

    public String getEid() throws Exception {
        Log.debug(TAG, "Getting the EID of the eUICC...");

        GetEuiccDataResponse getEuiccDataResponse = es10Interface.es10c_getEid();

        return getEuiccDataResponse.getEidValue().toString();
    }
}
