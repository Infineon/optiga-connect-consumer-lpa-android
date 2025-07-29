/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.enums;

@SuppressWarnings("unused")
public class CancelSessionReasons {
    public static final long END_USER_REJECTION = 0;
    public static final long POSTPONED = 1;
    public static final long TIMEOUT = 2;
    public static final long PPR_NOT_ALLOWED = 3;
    public static final long METADATA_MISMATCH = 4;
    public static final long LOAD_BPP_EXECUTION_ERROR = 5;
    public static final long UNDEFINED = 127;
}
