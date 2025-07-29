/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es10.definitions;

import com.beanit.jasn1.ber.types.BerBitString;

public class ResetOptions {
    public static BerBitString getResetOptions(boolean deleteOperationalProfiles,
                                               boolean deleteFieldLoadedTestProfiles,
                                               boolean resetDefaultSmdpAddress) {

        return new BerBitString(new boolean[]{deleteOperationalProfiles, deleteFieldLoadedTestProfiles, resetDefaultSmdpAddress});
    }
}
