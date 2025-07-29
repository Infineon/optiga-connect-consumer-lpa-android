/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.local;


import com.beanit.jasn1.ber.types.BerBoolean;
import com.gsma.sgp.messages.rspdefinitions.DisableProfileResponse;
import com.gsma.sgp.messages.rspdefinitions.Iccid;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;


public class DisableProfileWorker {
    private static final String TAG = DisableProfileWorker.class.getName();

    private final Es10Interface es10Interface;

    public DisableProfileWorker(Es10Interface es10Interface) {
        this.es10Interface = es10Interface;
    }

    public int disable(String iccidString) throws Exception {
        Log.debug(TAG,"Disabling profile with ICCID: " + iccidString);

        Iccid iccid = new Iccid(Bytes.decodeHexString(iccidString));
        BerBoolean refreshFlag = new BerBoolean(true);

        DisableProfileResponse disableProfileResponse = es10Interface.es10c_disableProfileByIccid(iccid,refreshFlag);

        return disableProfileResponse.getDisableResult().intValue();
    }
}
