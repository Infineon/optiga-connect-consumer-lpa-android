/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.identive;


import com.infineon.esim.lpa.core.dtos.enums.ProfileActionType;
import com.infineon.esim.lpa.euicc.EuiccConnectionSettings;
import com.infineon.esim.lpa.euicc.base.EuiccConnection;
import com.infineon.esim.lpa.euicc.base.generic.ISO7816Channel;
import com.infineon.esim.util.Log;

import java.util.List;

public class IdentiveEuiccConnection implements EuiccConnection, ISO7816Channel.ApduTransmitter {
    private static final String TAG = IdentiveEuiccConnection.class.getName();

    private final String euiccName;
    private final IdentiveCard identiveCard;
    private final ISO7816Channel iso7816Channel;

    private EuiccConnectionSettings euiccConnectionSettings;

    public IdentiveEuiccConnection(IdentiveCard identiveCard, String euiccName) {
        this.identiveCard = identiveCard;
        this.euiccName = euiccName;
        this.iso7816Channel = new ISO7816Channel(this);
    }

    @Override
    public void updateEuiccConnectionSettings(EuiccConnectionSettings euiccConnectionSettings) {
        this.euiccConnectionSettings = euiccConnectionSettings;
    }

    @Override
    public String getEuiccName() {
        return euiccName;
    }


    @Override
    public boolean open() throws Exception {
        Log.debug(TAG, "Opening Identive interface...");

        // Open connection to card
        identiveCard.connectCard(euiccName);

        // Open (logical) channel to ISD-R
        iso7816Channel.openChannel(euiccConnectionSettings);

        Log.debug(TAG, "Opening Identive interface result: " + isOpen());
        return isOpen();
    }

    @Override
    public void close() throws Exception {
        Log.debug(TAG, "Closing Identive eUICC connection...");
        if(isOpen()) {
            // Close (logical) channel
            iso7816Channel.closeChannel(euiccConnectionSettings);

            // Disconnect card
            identiveCard.disconnectCard();
        }
    }

    @Override
    public boolean isOpen() {
        return identiveCard.isConnected();
    }

    @Override
    public int configureMEP(String iccid, ProfileActionType profileAction) {
        return 0;
    }

    @Override
    public boolean resetEuicc() throws Exception {
        Log.debug(TAG, "Resetting card.");

        // Close (logical) channel
        iso7816Channel.closeChannel(euiccConnectionSettings);

        // Reset card
        identiveCard.resetCard();

        // Open (logical) channel
        iso7816Channel.openChannel(euiccConnectionSettings);

        return isOpen();
    }

    @Override
    public List<String> transmitAPDUS(List<String> apdus) throws Exception {
        if(!isOpen()) {
            open();
        }

        return iso7816Channel.transmitAPDUS(apdus);
    }

    @Override
    public byte[] transmit(byte[] command) {
        return identiveCard.transmitToCard(command);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }

}
