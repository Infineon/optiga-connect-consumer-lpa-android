/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.local;


import java.util.HashMap;
import java.util.List;

public class ClearNotificationsResult implements OperationResult {
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

    private final List<Integer> values;

    public ClearNotificationsResult(List<Integer> results) {
        this.values = results;
    }

    @Override
    public boolean isOk() {
        for(Integer value : values) {
            if(value != OK) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean equals(int value) {
        for(Integer internalValue : values) {
            if(value == internalValue) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String getDescription() {
        StringBuilder description = new StringBuilder();

        for(Integer value : values) {
            description.append(lookup.get(value)).append("\n");
        }

        return description.toString();
    }
}
