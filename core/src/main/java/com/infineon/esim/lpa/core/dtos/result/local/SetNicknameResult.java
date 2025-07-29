/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.local;


import java.util.HashMap;

public class SetNicknameResult implements OperationResult {
    public static final int OK = 0;
    public static final int ICCID_NOT_FOUND = 1;
    public static final int UNDEFINED_ERROR = 127;
    public static final int NONE = 128;

    private static final HashMap<Integer, String> lookup;

    static {
        lookup = new HashMap<>();
        lookup.put(OK,"OK");
        lookup.put(ICCID_NOT_FOUND, "ICCID not found.");
        lookup.put(UNDEFINED_ERROR, "Undefined error.");
        lookup.put(NONE, "No error code available.");
    }

    private final int value;

    public SetNicknameResult(int result) {
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
