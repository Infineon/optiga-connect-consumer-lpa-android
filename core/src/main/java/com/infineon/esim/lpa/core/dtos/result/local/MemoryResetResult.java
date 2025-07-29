/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.local;


import java.util.HashMap;

public class MemoryResetResult implements OperationResult {
    public static final int OK = 0;
    public static final int NOTHING_TO_DELETE = 1;
    public static final int CAT_BUSY = 5;
    public static final int UNDEFINED_ERROR = 127;
    public static final int NONE = 128;

    private static final HashMap<Integer, String> lookup;

    static {
        lookup = new HashMap<>();
        lookup.put(OK,"OK");
        lookup.put(NOTHING_TO_DELETE, "Nothing to delete.");
        lookup.put(CAT_BUSY, "CAT busy.");
        lookup.put(UNDEFINED_ERROR, "Undefined error.");
        lookup.put(NONE, "No error code available.");
    }

    private final int value;

    public MemoryResetResult(int result) {
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
