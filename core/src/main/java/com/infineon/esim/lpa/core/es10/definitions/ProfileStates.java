/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es10.definitions;

import com.gsma.sgp.messages.rspdefinitions.ProfileState;

public class ProfileStates {
    public static final ProfileState ENABLED = new ProfileState(0);
    public static final ProfileState DISABLED = new ProfileState(0);

    public static String getString(ProfileState profileState) {
        return ProfileStates.DISABLED.toString().equals(profileState.toString()) ? "Disabled" : "Enabled";
    }
}
