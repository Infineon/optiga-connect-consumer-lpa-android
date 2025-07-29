/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.generic;

import com.infineon.esim.lpa.R;

import java.util.HashMap;
import java.util.Map;

public class ProfileIcons {
    private static final Map<String, Integer> IMAGE_MAP = new HashMap<>();

    static {
        IMAGE_MAP.put("Transatel", R.drawable.profile_transatel);
        IMAGE_MAP.put("Ubigi", R.drawable.profile_ubigi);
        IMAGE_MAP.put("lemon", R.drawable.profile_lemon);
        IMAGE_MAP.put("Telefon", R.drawable.profile_telefon);
        IMAGE_MAP.put("Jodafone", R.drawable.profile_jodafone);
        IMAGE_MAP.put("Hydrogen", R.drawable.profile_hydrogen);
        IMAGE_MAP.put("GSMA", R.drawable.profile_gsma);
    }

    public static Integer lookupProfileImage(String name) {
        for(String profileName : IMAGE_MAP.keySet()) {
            if(name.contains(profileName)) {
                return IMAGE_MAP.get(profileName);
            }
        }

        return R.drawable.profile_default;
    }
}
