/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.util.android;

public interface InternetConnectionConsumer {
    void onConnected();
    void onDisconnected();
}
