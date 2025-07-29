/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es10.base;

import com.beanit.jasn1.ber.types.BerType;
import com.infineon.esim.messages.Ber;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Strings;

import java.util.ArrayList;
import java.util.List;

public class TransportCommand {
    private static final String TAG = TransportCommand.class.getName();

    private static final String CLA = "81";
    private static final String INS = "E2";
    private static final String P1_11 = "11";
    private static final String P1_91 = "91";

    private static final int MAX_DATA_LENGTH_BYTE = 240;

    /*
    See GSMA SGP.21 chapter 5.7.2 Transport Command for more details
     */

    public static List<String> getTransportCommands(BerType berData) {
        return getTransportCommands(Ber.getEncodedAsHexString(berData));
    }

    public static List<String> getTransportCommands(List<String> encodedDataList) {
        List<String> transportCommands = new ArrayList<>();
        for(String encodedData : encodedDataList) {
            transportCommands.addAll(getTransportCommands(encodedData));
        }
        return transportCommands;
    }

    public static List<String> getTransportCommands(String encodedData) {
        if(encodedData == null || encodedData.isEmpty()) {
            return null;
        }

        List<String> commandList = new ArrayList<>();

//        Log.info(TAG, "Full transport command: " + encodedData);

        List<String> subcommandList = Strings.splitByLength(encodedData, MAX_DATA_LENGTH_BYTE);

        for(int i = 0; i < subcommandList.size(); i++) {
            String encodedCommandPart = subcommandList.get(i);
            String encodedLength = Bytes.encodeHexString(Ber.encodeLength(encodedCommandPart.length() / 2));
            String p2 = Bytes.encodeHexString(Bytes.toByteArray(i));

            commandList.add(CLA + INS + P1_11 + p2 + encodedLength + encodedCommandPart);
        }

        // Replace the P1 of the last command with P1_91
        int lastCommandIndex = commandList.size() - 1;
        commandList.set(lastCommandIndex,Strings.replaceRegion(commandList.get(lastCommandIndex), P1_91, 4));

        return commandList;
    }
}
