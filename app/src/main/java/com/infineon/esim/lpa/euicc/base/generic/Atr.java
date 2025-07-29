/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base.generic;

import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SuppressWarnings("unused")
public class Atr {
    private static final String TAG = Atr.class.getName();

    // Allowed/valid ATRs of tested eUICCs. Extend on own risk.
    private static final List<String> VALID_ATR_PARTS = new ArrayList<>(Arrays.asList(
            "3B9F96803FC7828031E073F6215757A44D000560700014",
            "3B9F96803FC7828031E073F62157574A4D0005608000E4",
            "3B9F96803FC7828031E073F62157574A4D0005609000F4",
            "3B9F96803FC7828031E073F62157574A4D020B60010069",
            "3BDF96008091FE3F4C838031E073FE2117634950F4830F",
            "3BDF96008091FE3F4C838031E073FE211763495000830F",
            "3BDF96008091FE3F4C838031E073FE2117634950F6830F",
            "3BDF96008091FE3F4C838031E073FE211763495001830F" // OC1230 ATR
    ));

    public static Boolean isAtrValid(byte[] atr) {
        if (atr != null) {
            String atrStr = Bytes.encodeHexString(atr);
            Log.verbose(TAG, "ATR part for comparison: " + atrStr);

            for(String validAtrPart : VALID_ATR_PARTS) {
                if(atrStr.contains(validAtrPart)) {
                    return true;
                }
            }
        }
        return false;
    }
}
