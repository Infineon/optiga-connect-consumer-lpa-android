/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.local;

public interface OperationResult {

    boolean isOk();
    boolean equals(int value);
    String getDescription();
}
