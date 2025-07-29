/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.identive;

import android.content.Context;

import com.infineon.esim.lpa.euicc.base.EuiccConnection;
import com.infineon.esim.lpa.euicc.base.EuiccService;
import com.infineon.esim.util.Log;

import java.util.List;

public class IdentiveService implements EuiccService {
    private static final String TAG = IdentiveService.class.getName();

    private final IdentiveCard identiveCard;

    private IdentiveEuiccConnection identiveEuiccConnection;
    private boolean isConnected;

    public IdentiveService(Context context) {
        this.identiveCard = new IdentiveCard(context);
        this.isConnected = false;
    }

    public List<String> refreshEuiccNames() {
        Log.debug(TAG, "Refreshing Identive eUICC names...");
        return identiveCard.getReaderNames();
    }

    public void connect() throws Exception {
        Log.debug(TAG, "Opening connection to Identive service...");
        identiveCard.establishContext();

        isConnected = true;
    }

    public void disconnect() throws Exception {
        Log.debug(TAG, "Closing connection to Identive service...");
        identiveCard.releaseContext();

        isConnected = false;
    }

    public boolean isConnected() {
        return isConnected;
    }

    public EuiccConnection openEuiccConnection(String euiccName) {

        if(identiveEuiccConnection != null && euiccName.equals(identiveEuiccConnection.getEuiccName())) {
            Log.debug(TAG,"eUICC is already connected. Return existing eUICC connection.");
            return identiveEuiccConnection;
        }

        identiveEuiccConnection = new IdentiveEuiccConnection(identiveCard, euiccName);

        return identiveEuiccConnection;
    }
}
