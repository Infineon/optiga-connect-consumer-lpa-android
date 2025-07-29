/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.local;

import com.gsma.sgp.messages.rspdefinitions.Iccid;
import com.gsma.sgp.messages.rspdefinitions.SetNicknameResponse;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;

public class SetNicknameWorker {
    private static final String TAG = SetNicknameWorker.class.getName();

    private final Es10Interface es10Interface;

    public SetNicknameWorker(Es10Interface es10Interface) {
        this.es10Interface = es10Interface;
    }

    public int setNickname(String iccidString, String nickname) throws Exception {
        Log.debug(TAG, "Setting nickname...");

        Iccid iccid = new Iccid(Bytes.decodeHexString(iccidString));

        SetNicknameResponse setNicknameResponse = es10Interface.es10c_setNickname(iccid, nickname);

        return setNicknameResponse.getSetNicknameResult().intValue();
    }
}
