/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es10.definitions;

import com.gsma.sgp.messages.rspdefinitions.ProfileClass;

public class ProfileClasses {
    public static final ProfileClass TEST = new ProfileClass(0);
    public static final ProfileClass PROVISIONING = new ProfileClass(1);
    public static final ProfileClass OPERATIONAL = new ProfileClass(2);
}
