/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base;

import java.util.List;

@SuppressWarnings("unused")
public interface EuiccService {

    List<String> refreshEuiccNames();

    void connect() throws Exception;
    void disconnect() throws Exception;

    boolean isConnected();

    EuiccConnection openEuiccConnection(String euiccName) throws Exception ;
}
