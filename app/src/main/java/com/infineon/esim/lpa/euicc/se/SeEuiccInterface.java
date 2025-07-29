/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.se;

import android.content.Context;
import android.content.pm.PackageManager;

import com.infineon.esim.lpa.Application;
import com.infineon.esim.lpa.euicc.base.EuiccConnection;
import com.infineon.esim.lpa.euicc.base.EuiccInterface;
import com.infineon.esim.lpa.euicc.base.EuiccInterfaceStatusChangeHandler;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.List;

final public class SeEuiccInterface implements EuiccInterface {
    private static final String TAG = SeEuiccInterface.class.getName();

    public static final String INTERFACE_TAG = "SE";

    private final SeService seService;
    private final List<String> euiccNames;

    public SeEuiccInterface(Context context, EuiccInterfaceStatusChangeHandler euiccInterfaceStatusChangeHandler) {
        Log.debug(TAG, "Constructor of SeEuiccReader.");

        this.seService = new SeService(context, euiccInterfaceStatusChangeHandler);
        this.euiccNames = new ArrayList<>();
    }

    @Override
    public String getTag() {
        return INTERFACE_TAG;
    }

    @Override
    public boolean isAvailable() {
        PackageManager packageManager = Application.getPacketManager();
        boolean isAvailable = false;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.R) {
            // Starting with Android R the existence of OMAPI can be checked
            isAvailable = packageManager.hasSystemFeature(PackageManager.FEATURE_SE_OMAPI_UICC) |
                    packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        } else {
            // Starting with Android P the OMAPI is part of Android but cannot be checked (yet).
            isAvailable = true;
        }

        Log.debug(TAG, "Checking if SE eUICC interface is available: " + isAvailable);

        return isAvailable;
    }

    @Override
    public boolean isInterfaceConnected() {
        boolean isConnected = false;
        if(isAvailable()) {
            isConnected = seService.isConnected();
        }

        return isConnected;
    }

    @Override
    public boolean connectInterface() throws Exception {
        seService.connect();

        return seService.isConnected();
    }

    @Override
    public boolean disconnectInterface() throws Exception {

        if(seService.getEuiccConnection() != null) {
            seService.closeEuiccConnection();
        }

        if(seService != null) {
            seService.disconnect();
        }

        euiccNames.clear();

        return true;
    }

    @Override
    public List<String> refreshEuiccNames() {
        Log.debug(TAG, "Refreshing SE eUICC names...");
        euiccNames.clear();
        
        if(isInterfaceConnected()) {
            euiccNames.addAll(seService.refreshEuiccNames());
        }

        return euiccNames;
    }

    @Override
    public List<String> getEuiccNames() {
        return euiccNames;
    }

    @Override
    public EuiccConnection getEuiccConnection(String euiccName) throws Exception {

        EuiccConnection euiccConnection = seService.getEuiccConnection();

        if(euiccConnection == null || !euiccConnection.getEuiccName().equals(euiccName) || seService.updateInterfaceSetting()) {
            // Close the old eUICC connection if it is with another eUICC
            if(euiccConnection != null) {
                seService.closeEuiccConnection();
            }

            // Open new eUICC connection
            euiccConnection = seService.openEuiccConnection(euiccName);
        }

        return euiccConnection;
    }
}
