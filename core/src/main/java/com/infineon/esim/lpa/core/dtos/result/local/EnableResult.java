/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.local;

import java.util.HashMap;

public class EnableResult implements  OperationResult {
    public static final int OK = 0;
    public static final int ICCID_OR_AID_NOT_FOUND = 1;
    public static final int PROFILE_NOT_IN_DISABLED_STATE = 2;
    public static final int DISALLOWED_BY_POLICY = 3;
    public static final int WRONG_PROFILE_REENABLING = 4;
    public static final int CAT_BUSY = 5;
    public static final int UNDEFINED_ERROR = 127;
    public static final int NONE = 128;

    private static final HashMap<Integer, String> lookup;

    static {
        lookup = new HashMap<>();
        lookup.put(OK,"OK");
        lookup.put(ICCID_OR_AID_NOT_FOUND, "ICCID or AID not found.");
        lookup.put(PROFILE_NOT_IN_DISABLED_STATE, "Profile not in disabled state.");
        lookup.put(DISALLOWED_BY_POLICY, "Disallowed by policy.");
        lookup.put(WRONG_PROFILE_REENABLING, "Wrong profile reenabling.");
        lookup.put(CAT_BUSY, "CAT busy.");
        lookup.put(UNDEFINED_ERROR, "Undefined error.");
        lookup.put(NONE, "No error code available.");
    }

    private final int value;

    public EnableResult(int result) {
        this.value = result;
    }

    @Override
    public boolean isOk() {
        return value == OK;
    }

    @Override
    public boolean equals(int value) {
        return this.value == value;
    }

    @Override
    public String getDescription() {
        return lookup.get(value);
    }
}
