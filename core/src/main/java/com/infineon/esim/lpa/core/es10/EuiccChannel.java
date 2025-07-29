/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es10;


import java.util.List;

public interface EuiccChannel {
    List<String> transmitAPDUS(List<String> apdus) throws Exception;
}
