/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.util.android;

import android.net.wifi.WifiManager;

import com.infineon.esim.lpa.Application;

public class WifiStatus {

    public static boolean isWifiEnabled() {
        WifiManager wifiManager = Application.getWifiManager();

        return wifiManager.isWifiEnabled();
    }
}
