/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es10.base;

import com.gsma.sgp.messages.rspdefinitions.DeleteProfileRequest;
import com.gsma.sgp.messages.rspdefinitions.Iccid;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;

import org.junit.jupiter.api.Test;

import java.util.List;

public class TransportCommandTest {
    private static final String TAG = TransportCommandTest.class.getName();

    @Test
    public void testGetTransportCommands() {
        Iccid iccid = new Iccid(Bytes.decodeHexString("98000000000000001290"));
        DeleteProfileRequest deleteProfileRequest = new DeleteProfileRequest();
        deleteProfileRequest.setIccid(iccid);

        List<String> transportCommands = TransportCommand.getTransportCommands(deleteProfileRequest);

        for(String transportCommand : transportCommands) {
            Log.debug(TAG, "Transport command: " + transportCommand);
        }
    }
}