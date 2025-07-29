/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.util;

import java.util.ArrayList;
import java.util.List;

public class Strings {
    private static final String TAG = Strings.class.getName();


    public static List<String> splitByLength(String input, int partLength) {
        List<String> output = new ArrayList<>();

        for(int i = 0; i < input.length(); i += partLength) {
            output.add(input.substring(i, Math.min(i+partLength,input.length())));
        }

        return output;
    }

    public static String replaceRegion(String input, String replace, int index) {
        StringBuilder stringBuffer = new StringBuilder(input);

        if(input.length() > index) {
            int end = Math.min(input.length(), index + replace.length());
            int replaceLength = Math.min(replace.length(), end - index);
            stringBuffer.replace(index, end, replace.substring(0, replaceLength));
            return stringBuffer.toString();
        } else {
            return input;
        }
    }

    public static boolean isNotBlankOrEmpty(String input) {
        if(input != null) {
            return !input.trim().isEmpty();
        } else {
            return false;
        }
    }
}
