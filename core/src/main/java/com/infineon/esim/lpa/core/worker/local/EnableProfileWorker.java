/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.local;


import com.beanit.jasn1.ber.types.BerBoolean;
import com.gsma.sgp.messages.rspdefinitions.EnableProfileResponse;
import com.gsma.sgp.messages.rspdefinitions.Iccid;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;

public class EnableProfileWorker {
    private static final String TAG = EnableProfileWorker.class.getName();

    private final Es10Interface es10Interface;

    public EnableProfileWorker(Es10Interface es10Interface) {
        this.es10Interface = es10Interface;
    }

    public int enable(String iccidString, boolean refreshFlag) throws Exception {
        Log.debug(TAG, "Enabling profile with ICCID: " + iccidString);

        Iccid iccid = new Iccid(Bytes.decodeHexString(iccidString));

        EnableProfileResponse enableProfileResponse = es10Interface.es10c_enableProfileByIccid(iccid, new BerBoolean(refreshFlag));

        return enableProfileResponse.getEnableResult().intValue();
    }
}
