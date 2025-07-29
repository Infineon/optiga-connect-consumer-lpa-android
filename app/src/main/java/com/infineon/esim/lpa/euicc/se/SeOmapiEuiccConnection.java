/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.se;

import android.content.Context;
import android.se.omapi.Channel;
import android.se.omapi.Reader;
import android.se.omapi.Session;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;

import com.infineon.esim.lpa.core.dtos.enums.ProfileActionType;
import com.infineon.esim.lpa.euicc.EuiccConnectionSettings;
import com.infineon.esim.lpa.euicc.base.EuiccConnection;
import com.infineon.esim.lpa.euicc.base.generic.Atr;
import com.infineon.esim.lpa.euicc.base.generic.Definitions;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;

import java.io.IOException;
import java.lang.reflect.Method;
import java.security.AccessControlException;
import java.util.ArrayList;
import java.util.List;

public class SeOmapiEuiccConnection implements EuiccConnection {
    private static final String TAG = SeOmapiEuiccConnection.class.getName();

    private final Reader reader;

    private Session session;
    private Channel channel;

    private EuiccConnectionSettings euiccConnectionSettings;

    public SeOmapiEuiccConnection(Reader reader) {
        Log.debug(TAG, "New SeOmapiEuiccConnection: " + reader.getName());
        this.reader = reader;
    }

    @Override
    public void updateEuiccConnectionSettings(EuiccConnectionSettings euiccConnectionSettings) {
        this.euiccConnectionSettings = euiccConnectionSettings;
    }

    @Override
    public String getEuiccName() {
        return reader.getName();
    }


    @Override
    public boolean resetEuicc() throws Exception {
        Log.debug(TAG, "Resetting the eUICC.");

        // Close the connection first
        close();

        // Wait for the phone to detect the profile change
        try {
            Thread.sleep(euiccConnectionSettings.getProfileInitializationTime());
        } catch (Exception e) {
            Log.error(Log.getFileLineNumber() + " " + e.getMessage());
        }

        // Open the connection again
        return open();
    }

    @Override
    public boolean open() throws Exception {
        Log.debug(TAG, "Opening connection for eUICC " + reader.getName());
        try {
            if (session == null || session.isClosed()) {
                Log.debug(TAG, "Opening a new session...");
                session = reader.openSession();
                if(session != null) {
                    Log.debug(TAG, "Successfully opened a new session.");
                } else {
                    Log.error(TAG, "Failed to open a new session.");
                    return false;
                }

                if(!Atr.isAtrValid(session.getATR())) {
                    Log.error(TAG, "eUICC not allowed!");
                    close();
                    throw new Exception("eUICC not allowed!");
                }
            }

            if (channel == null || !channel.isOpen()) {
                Log.debug(TAG, "Opening a new logical channel...");
                channel = session.openLogicalChannel(Bytes.decodeHexString(Definitions.ISDR_AID));
                Log.debug(TAG, "Opened logical channel: " + Bytes.encodeHexString(channel.getSelectResponse()));
            }
        } catch (Exception e) {
            if( e instanceof IOException || e instanceof AccessControlException)
                Log.error(TAG, "Opening eUICC connection failed: " + e.toString()); //no EUICC present or no access rights -- we cannot access channel
            else
                throw new Exception("Opening eUICC connection failed.", e); //something else, handle in upper layers

        }

        if(channel != null) {
            return channel.isOpen();
        } else {
            return false;
        }
    }

    @Override
    public void close() {
        Log.debug(TAG, "Closing connection for eUICC " + reader.getName());

        if(isOpen()) {
            if (channel != null) {
                channel.close();
            }
            if (session != null) {
                session.close();
            }
        }
    }

    @Override
    public boolean isOpen() {
        if(channel == null) {
            return false;
        }

        return channel.isOpen();
    }

    @Override
    public int configureMEP(String iccid, ProfileActionType profileAction) {
        return 0;
    }

    @Override
    public List<String> transmitAPDUS(List<String> apdus) throws Exception {

        if(!isOpen()) {
            open();
        }

        List<String> responses = new ArrayList<>();

        for(String apdu : apdus) {
            byte[] command = Bytes.decodeHexString(apdu);

            byte[] response = channel.transmit(command);
            responses.add(Bytes.encodeHexString(response));
        }

        return responses;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}
