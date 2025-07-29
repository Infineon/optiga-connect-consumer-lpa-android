/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base;

import com.infineon.esim.lpa.core.dtos.enums.ProfileActionType;
import com.infineon.esim.lpa.core.es10.EuiccChannel;
import com.infineon.esim.lpa.euicc.EuiccConnectionSettings;


@SuppressWarnings("unused")
public interface EuiccConnection extends EuiccChannel {

    void updateEuiccConnectionSettings(EuiccConnectionSettings euiccConnectionSettings);

    String getEuiccName();

    boolean open() throws Exception;
    void close() throws Exception;
    boolean isOpen();

    int configureMEP(String iccid, ProfileActionType profileAction) throws Exception;

    boolean resetEuicc() throws Exception;
}
