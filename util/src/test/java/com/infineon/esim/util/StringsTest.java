/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.util;

import org.junit.jupiter.api.Test;

public class StringsTest {
    private static final String TAG = StringsTest.class.getName();

    @Test
    public void testSplitByLength() {
        String input = "AAAAAAAAAABBBBBBBBBBCCCCCCCCCCD";

        for(String part : Strings.splitByLength(input, 10)) {
            Log.debug(TAG, part);
        }
    }

    @Test
    public void testReplaceRegion() {
        String input = "AAAAAAAAAABBBBBBBBBBCCCCCCCCCCD";

        String replace = "XX";

        Log.debug(TAG, "Original string: " + input);
        Log.debug(TAG, "Replaced string: " + Strings.replaceRegion(input,replace,30));
    }
}