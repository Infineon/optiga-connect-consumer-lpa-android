/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base;

import java.util.List;

@SuppressWarnings("unused")
public interface EuiccInterfaceStatusChangeHandler {

    void onEuiccInterfaceConnected(String interfaceTag);
    void onEuiccInterfaceDisconnected(String interfaceTag);

    void onEuiccConnected(String euiccName, EuiccConnection euiccConnection);
    void onEuiccListRefreshed(List<String> euiccDescriptions);
}
