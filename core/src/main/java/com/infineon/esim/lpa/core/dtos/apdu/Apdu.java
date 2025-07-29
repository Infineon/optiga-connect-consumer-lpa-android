/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.apdu;

import java.util.List;

public class Apdu {
    private static final String TAG = Apdu.class.getName();

    // Common status words
    private static final String SW_SUCCESS_STATUS_WORD =            "9000";
    private static final String SW_SUCCESS_PROACTIVE_CMD_WAITING =  "91";

    public static boolean isSuccessResponse(String response) {
        if((response == null) || (response.length() < 4)) {
            return false;
        }

        String statusWord = response.substring(response.length()-4);

        return statusWord.equals(SW_SUCCESS_STATUS_WORD)
                || statusWord.startsWith(SW_SUCCESS_PROACTIVE_CMD_WAITING);

    }

    public static String getStatusWord(String response) {
        if(response.length() >= 4) {
            return response.substring(response.length() - 4);
        } else {
            return null;
        }
    }

    public static boolean isLastResponseSuccess(List<String> responses) {
        if(responses.size() == 0) {
            return false;
        }

        String lastResponse = responses.get(responses.size() - 1);
        return isSuccessResponse(lastResponse);
    }

    public static boolean doesResponseContainData(String response) {
        return response.length() > 4;
    }
}
