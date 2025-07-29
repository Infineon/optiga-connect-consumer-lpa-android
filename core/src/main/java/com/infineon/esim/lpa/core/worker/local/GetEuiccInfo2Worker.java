/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.local;

import com.gsma.sgp.messages.rspdefinitions.EUICCInfo2;
import com.infineon.esim.lpa.core.dtos.EuiccInfo;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.util.Log;

public class GetEuiccInfo2Worker {
    private static final String TAG = GetEuiccInfo2Worker.class.getName();

    private final Es10Interface es10Interface;

    public GetEuiccInfo2Worker(Es10Interface es10Interface) {
        this.es10Interface = es10Interface;
    }

    public EuiccInfo getEuiccInfo2() throws Exception {
        Log.debug(TAG, "Getting EuiccInfo2...");

        EUICCInfo2 euiccInfo2 = es10Interface.es10b_getEuiccInfo2();

        return new EuiccInfo(euiccInfo2);
    }
}
