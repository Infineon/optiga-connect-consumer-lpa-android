/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.se;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.se.omapi.Reader;
import android.se.omapi.SEService;
import android.se.omapi.Session;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;

import androidx.core.content.ContextCompat;

import com.infineon.esim.lpa.euicc.base.EuiccConnection;
import com.infineon.esim.lpa.euicc.base.EuiccInterfaceStatusChangeHandler;
import com.infineon.esim.lpa.euicc.base.EuiccService;
import com.infineon.esim.lpa.euicc.base.generic.Atr;
import com.infineon.esim.util.Log;
import com.infineon.esim.lpa.data.Preferences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeoutException;

public class SeService implements EuiccService {
    private static final String TAG = SeService.class.getName();

    private final static String UICC_READER_PREFIX = "SIM";
    private static final long SERVICE_CONNECTION_TIME_OUT = 4000;

    private final Context context;
    private final Object seServiceMutex;

    private final EuiccInterfaceStatusChangeHandler euiccInterfaceStatusChangeHandler;

    private SEService seService; // OMAPI / Secure Element

    private EuiccConnection euiccConnection; // EuiccConnection cache

    public SeService(Context context, EuiccInterfaceStatusChangeHandler euiccInterfaceStatusChangeHandler) {
        this.context = context;
        this.euiccInterfaceStatusChangeHandler = euiccInterfaceStatusChangeHandler;
        this.seServiceMutex = new Object();
    }

    public EuiccConnection getEuiccConnection() {
        return euiccConnection;
    }

    private List<String> getEuiccNamesTelephony() {
        List<String> euiccNames = new ArrayList<>();

        // If we don't have LPA permissions, no MEP is possible and we try using OMAPI.
        int permissionState = ContextCompat.checkSelfPermission(context, Manifest.permission.MODIFY_PHONE_STATE);
        if (permissionState != PackageManager.PERMISSION_GRANTED) {
            Log.error(TAG, "Cannot access telephony manager; no permission granted.");
            return euiccNames;
        }

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            Log.error(TAG, "Cannot access telephony manager; not supported by Android version.");
            return euiccNames;
        }

        // Get telephony manager to retrieve CardInfos
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        List<UiccCardInfo> cardInfos = telephonyManager.getUiccCardsInfo();

        // Add available, removable slots to list
        if (cardInfos != null) {
            for (UiccCardInfo info : cardInfos) {
                if (info != null && info.isRemovable() && !info.getPorts().isEmpty()) { // only removable eSIMs
                    euiccNames.add(String.format("Slot %d", info.getPorts().iterator().next().getLogicalSlotIndex())); //select first logical slot of port list
                }
            }
        }

        return euiccNames;
    }

    private List<String> getEuiccNamesOmapi() {
        List<String> euiccNames = new ArrayList<>();

        for (Reader reader : seService.getReaders()) {
            if (reader.getName().startsWith(UICC_READER_PREFIX)) { // use only removable eSIM
                if (isReaderAllowed(reader)) {                     // use only eSIMs with known ATR
                    String euiccName = reader.getName();
                    Log.debug(TAG, " - " + euiccName);
                    euiccNames.add(euiccName);
                }
            }
        }
        return euiccNames;
    }

    public List<String> refreshEuiccNames() {
        Log.debug(TAG, "Refreshing eUICC names...");
        List<String> euiccNames = new ArrayList<>();

        Log.debug(TAG, "Refresh using TelephonyManager");
        euiccNames = getEuiccNamesTelephony();
        if (!euiccNames.isEmpty())
            return euiccNames;

        //if we did not get any eUICCs (for whatever reason),just try using OMAPI
        Log.debug(TAG, "Fallback: Refresh using OMAPI");
        euiccNames = getEuiccNamesOmapi();
        return euiccNames;
    }

    public void connect() throws TimeoutException {
        Log.debug(TAG, "Opening connection to SE service...");

        // Initialize secure element if not available
        if (seService == null) {
            initializeConnection();
        }

        // Connect to secure element if connection is not already established
        if (seService.isConnected()) {
            Log.debug(TAG, "SE connection is already open.");
        } else {
            // Connect to secure element
            waitForConnection();
        }
    }

    public void disconnect() {
        Log.debug(TAG, "Closing connection to SE service...");

        if (seService != null && seService.isConnected()) {
            Log.debug(TAG, "Shutting down SE service.");
            seService.shutdown();
            seService = null;

            euiccInterfaceStatusChangeHandler.onEuiccInterfaceDisconnected(SeEuiccInterface.INTERFACE_TAG);
        }
    }

    public boolean isConnected() {
        if (seService == null) {
            return false;
        } else {
            return seService.isConnected();
        }
    }

    private void initializeConnection() {
        Log.debug(TAG, "Initializing SE connection.");

        seService = new SEService(context, Runnable::run, () -> {
            Log.debug(TAG, "SE service is connected!");
            synchronized (seServiceMutex) {
                seServiceMutex.notify();
            }

            euiccInterfaceStatusChangeHandler.onEuiccInterfaceConnected(SeEuiccInterface.INTERFACE_TAG);
        });
    }

    private void waitForConnection() throws TimeoutException {
        Log.debug(TAG, "Waiting for SE connection...");

        Timer connectionTimer = new Timer();
        connectionTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                synchronized (seServiceMutex) {
                    seServiceMutex.notifyAll();
                }
            }
        }, SERVICE_CONNECTION_TIME_OUT);

        synchronized (seServiceMutex) {
            if (!seService.isConnected()) {
                try {
                    seServiceMutex.wait();
                } catch (InterruptedException e) {
                    Log.error(TAG, "SE service could not be waited for.", e);
                }
            }
            if (!seService.isConnected()) {
                throw new TimeoutException(
                        "SE Service could not be connected after "
                                + SERVICE_CONNECTION_TIME_OUT + " ms.");
            }
            connectionTimer.cancel();
        }
    }

    public EuiccConnection openEuiccConnection(String euiccName) throws Exception {
        if (!seService.isConnected()) {
            throw new Exception("Secure element is not connected.");
        }

        if (euiccConnection != null && euiccName.equals(euiccConnection.getEuiccName()) && !updateInterfaceSetting()) {
            Log.debug(TAG, "eUICC is already connected. Return existing eUICC connection.");
            return euiccConnection;
        }

        if (!refreshEuiccNames().contains(euiccName)) {
            Log.error(TAG, "Cannot open session: eUICC reader not found.");
            throw new Exception("Cannot open session:  eUICC reader not found.");
        }

        if (euiccName.startsWith("Slot")) //we have access to telephony manager
        {
            int slotNumber = Character.getNumericValue(euiccName.charAt(euiccName.length() - 1));
            if (isMEPsupported(slotNumber) || Preferences.getForceTelephonyInterface())
                euiccConnection = new SeTelephonyEuiccConnection(context, euiccName);
            else {  //we still (try to) use OMAPI for communication, since there is anyway no MEP
                try {
                    euiccConnection = new SeOmapiEuiccConnection(seService.getUiccReader(slotNumber + 1)); //OMAPI slots start at 1
                } catch (Exception e) {
                    Log.error("Cannot open Omapi Connection: " + e.toString());
                    euiccConnection = new SeTelephonyEuiccConnection(context, euiccName); //try Telephony Interface as fallback (for Android Devkits, where no OMAPI is available)
                }
            }
            return euiccConnection;
        } else { //try OMAPI otherwise
            for (Reader reader : seService.getReaders()) {
                if (reader.getName().equals(euiccName)) {
                    euiccConnection = new SeOmapiEuiccConnection(reader);
                    return euiccConnection;
                }
            }
        }

        throw new Exception("Internal error"); //we should never reach this!
    }

    public boolean updateInterfaceSetting() {

        if (euiccConnection == null) // no open interface obviously requires an interface update
                return true;

        // Check if the interface is not correct
        return (((euiccConnection instanceof SeTelephonyEuiccConnection) && !Preferences.getForceTelephonyInterface()) || //this might actually be okay, but return true to reconnect anyway -- just in case
                ((euiccConnection instanceof SeOmapiEuiccConnection) && Preferences.getForceTelephonyInterface()));

    }

    public void closeEuiccConnection() throws Exception {
            euiccConnection.close();
            euiccConnection =null;
    }

    private boolean isMEPsupported(int slotNumber) {

        // Get telephony manager to retrieve CardInfos
        TelephonyManager telephonyManager =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        List<UiccCardInfo> cardInfos = telephonyManager.getUiccCardsInfo();  //we should anyway only be here if we're > API33. TODO: how to deal with warning?

        UiccCardInfo cardInfo = null;
        // Select cardInfo from our current slot
        if (cardInfos != null) {
            for (UiccCardInfo info : cardInfos) {
                if (info != null && !info.getPorts().isEmpty()) {
                    if (info.getPorts().iterator().next().getLogicalSlotIndex()  == slotNumber){
                        cardInfo = info;
                        break;
                    }
                }
            }
        }
        // check if MEP is supported via flag and via port list
        if (cardInfo != null) {

            if (cardInfo.isMultipleEnabledProfilesSupported())
                return true;

            Collection<UiccPortInfo> portInfos = cardInfo.getPorts();
            return (portInfos != null && portInfos.size() > 1);
        }

        return false;
    }

    private boolean isReaderAllowed(Reader reader) {

        try {
            Session session = reader.openSession();

            boolean isAllowed = Atr.isAtrValid(session.getATR());

            session.close();

            return isAllowed;
        } catch(Exception e) {
            return false;
        }
    }
}
