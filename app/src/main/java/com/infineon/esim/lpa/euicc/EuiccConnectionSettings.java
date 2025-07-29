/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc;

public class EuiccConnectionSettings {
    final boolean shallSendTerminalCapability;
    final boolean shallSendOpenLogicalChannel;
    final int profileInitializationTime;

    public EuiccConnectionSettings(boolean shallSendTerminalCapability, boolean shallSendOpenLogicalChannel, int profileInitializationTime) {
        this.shallSendTerminalCapability = shallSendTerminalCapability;
        this.shallSendOpenLogicalChannel = shallSendOpenLogicalChannel;
        this.profileInitializationTime = profileInitializationTime;
    }

    public boolean isShallSendTerminalCapability() {
        return shallSendTerminalCapability;
    }

    public boolean isShallSendOpenLogicalChannel() {
        return shallSendOpenLogicalChannel;
    }

    public int getProfileInitializationTime() {
        return profileInitializationTime;
    }
}
