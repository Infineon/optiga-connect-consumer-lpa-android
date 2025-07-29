/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base;

import java.util.List;

@SuppressWarnings("unused")
public interface EuiccInterface {

    String getTag();

    boolean isAvailable();
    boolean isInterfaceConnected();
    boolean connectInterface() throws Exception;
    boolean disconnectInterface() throws Exception;

    List<String> refreshEuiccNames() throws Exception;
    List<String> getEuiccNames();

    EuiccConnection getEuiccConnection(String euiccName) throws Exception;
}
