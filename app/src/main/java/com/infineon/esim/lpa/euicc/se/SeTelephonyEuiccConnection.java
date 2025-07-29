/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.se;

import android.content.Context;
import android.telephony.IccOpenLogicalChannelResponse;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.telephony.UiccCardInfo;
import android.telephony.UiccPortInfo;

import com.infineon.esim.lpa.core.dtos.enums.ProfileActionType;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.core.dtos.result.local.ConfigureMEPResult;
import com.infineon.esim.lpa.euicc.EuiccConnectionSettings;
import com.infineon.esim.lpa.euicc.base.EuiccConnection;
import com.infineon.esim.lpa.euicc.base.generic.Definitions;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SeTelephonyEuiccConnection implements EuiccConnection {
    private static final String TAG = SeTelephonyEuiccConnection.class.getName();

    private EuiccConnectionSettings euiccConnectionSettings;

    private TelephonyManager telephonyManager;

    private int euiccSlot;

    private int euiccPort;

    private UiccCardInfo euiccCardInfo;

    private String euiccName;

    private boolean MEPsupported;

    private IccOpenLogicalChannelResponse channelResponse;

    public SeTelephonyEuiccConnection(Context context, String name) throws Exception {
        Log.debug(TAG, "New SeTelephonyEuiccConnection: " + name);
        TelephonyManager tmgr =
                (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        telephonyManager = tmgr.createForSubscriptionId(SubscriptionManager.getDefaultSubscriptionId());

        Log.debug(TAG, "Created telephonyManager:" + telephonyManager.toString());

        euiccName = name;
        euiccSlot = Character.getNumericValue(name.charAt(name.length() - 1)); //TODO -- clean up?
        euiccCardInfo = getCardInfo(euiccSlot);
        if(euiccCardInfo != null)
        {
           // throw new Exception("No telephony interface for \"" + name + "\"");
            euiccPort = getEmptyPortIndex();
            if(euiccPort == -1) //no empty port available, choose any other port
                euiccPort = getPortIndexByIccid("any");

            MEPsupported = (euiccCardInfo.getPorts().size() > 1);
        }
        else {
            euiccPort = -1;
            MEPsupported = false;
        }
    }

    private UiccCardInfo getCardInfo(int slotNumber) {
        try {
            List<UiccCardInfo> cardInfos = telephonyManager.getUiccCardsInfo(); //we should anyway only be here if we're > API33. TODO: how to deal with warning?

            // Select cardInfo from our current slot
            if (cardInfos != null) {
                for (UiccCardInfo info : cardInfos) {
                    if (info != null && !info.getPorts().isEmpty() &&
                            info.getPorts().iterator().next().getLogicalSlotIndex() == slotNumber) {
                        return info;
                    }
                }
            }
        } catch (Exception e)
        {
            Log.error(TAG, "Cannot retrieve card info from Telephony Manager: " + e.toString());
        }
        return null;
    }
    private int getEmptyPortIndex()  {

        Collection<UiccPortInfo> portInfos = euiccCardInfo.getPorts(); //we should anyway only be here if we're > API33. TODO: how to deal with warning?

        for (UiccPortInfo pinfo : portInfos)
        {
            //we get the first empty port info
            if(pinfo.isActive() && pinfo.getIccId().isEmpty())
                return pinfo.getPortIndex();
        }
        return -1;
    }

    private int getPortIndexByIccid(String iccid) {

        Collection<UiccPortInfo> portInfos = euiccCardInfo.getPorts(); //we should anyway only be here if we're > API33. TODO: how to deal with warning?

        if(iccid.equals("any"))
        {
            for (UiccPortInfo pinfo : portInfos)
            {
                if(pinfo.isActive())
                    return pinfo.getPortIndex();
            }
            return 0;
        } else {
            for (UiccPortInfo pinfo : portInfos)
            {
                if(!pinfo.getIccId().isEmpty() && pinfo.isActive() && pinfo.getIccId().equals(iccid))
                    return pinfo.getPortIndex();
            }
            return -1;
        }
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
        Log.debug(TAG, "Opening connection for eUICC " + euiccName);

        if (channelResponse == null) {
            try {
              if(MEPsupported){
                    Method m2 = telephonyManager.getClass().getMethod("iccOpenLogicalChannelByPort",
                            int.class, int.class, String.class, int.class);
                    channelResponse = (IccOpenLogicalChannelResponse) m2.invoke(telephonyManager, euiccSlot,
                            euiccPort,
                            Definitions.ISDR_AID,
                            0x00);
              }
              else {
                  channelResponse = telephonyManager.iccOpenLogicalChannel(Definitions.ISDR_AID);
              }
            }
            catch (Exception e) { //TODO: specific error cases
                Log.error(TAG, "Opening eUICC connection failed.", e);
                return false;
                // throw new Exception("Opening eUICC connection failed.", e);
            }
            Log.debug(TAG, euiccName + ": Opened logical channel at port " + euiccPort);
        }
        else
            Log.debug(TAG, euiccName + ": Channel already open.");

        return (channelResponse.getStatus() == IccOpenLogicalChannelResponse.STATUS_NO_ERROR);

    }

    @Override
    public void close() throws Exception {
        Log.debug(TAG, "Closing connection for eUICC " + euiccName);

        if(channelResponse != null)
        {
            telephonyManager.iccCloseLogicalChannel(channelResponse.getChannel());
            channelResponse = null;
        }
    }

    @Override
    public boolean isOpen() {

        if(channelResponse == null) {
            return false;
        }

        return (channelResponse.getStatus() == IccOpenLogicalChannelResponse.STATUS_NO_ERROR);
    }

    @Override
    public int configureMEP(String iccid, ProfileActionType profileAction) throws Exception {
        int newPort = 0;
        switch (profileAction) {
            case PROFILE_ACTION_ENABLE: //To enable a profile in MEP, an empty port must be available
                if(MEPsupported) //only assign a new port if there are multiple available
                {
                    newPort = getEmptyPortIndex();
                    if(newPort == -1)
                        return ConfigureMEPResult.NO_PORT_AVAILABLE;
                }
                break;
            case PROFILE_ACTION_SET_NICKNAME:
            case PROFILE_ACTION_DELETE:
            case PROFILE_ACTION_DISABLE: //To disable a profile in MEP, the correct port must be selected
                if(MEPsupported) //only assign a new port if there are multiple available
                {
                    newPort = getPortIndexByIccid(iccid);
                    if(newPort == -1)
                        return ConfigureMEPResult.UNDEFINED_ERROR;
                }
                break;
        }
        if(newPort != euiccPort) {
            if (isOpen()) {
                close();
            }
            open();
        }
        return ConfigureMEPResult.OK;
    }


    @Override
    public List<String> transmitAPDUS(List<String> apdus) throws Exception {

        if(!isOpen()) {
            open();
        }

        List<String> responses = new ArrayList<>();

        for(String apdu : apdus) {

            byte[] apduBytes = Bytes.decodeHexString(apdu);
            String data = getDataFromApdu(apdu);
            int length =  (apduBytes.length > 4) ? apduBytes[4] & 0xFF : 0x00;

            String responseString = telephonyManager.iccTransmitApduLogicalChannel(channelResponse.getChannel(),
                    Byte.toUnsignedInt(apduBytes[0]),        // CLA
                    Byte.toUnsignedInt(apduBytes[1]),        // INS
                    Byte.toUnsignedInt(apduBytes[2]),        // P1
                    Byte.toUnsignedInt(apduBytes[3]),        // P2
                    length,                                  // Lc
                    data);

            int responseLength = responseString.length();
            String responseData = responseString.substring(0, responseLength - 4);
            String statusWord = responseString.substring(responseLength - 4);

            // create response buffer
            final StringBuilder dataBuilder = new StringBuilder(responseData);

            while (statusWord.startsWith("61")) {

                // issue get response command
                Log.debug(TAG, "transmitAPDUS: Get missing bytes.");

                responseString = telephonyManager.iccTransmitApduLogicalChannel(channelResponse.getChannel(),
                        apduBytes[0] & 0xFF,        // CLA
                        0xC0,        // INS
                        0x00,        // P1
                        0x00,        // P2
                        0x00,        // Lc
                        null);

                responseLength = responseString.length();
                responseData = responseString.substring(0, responseLength - 4);
                statusWord = responseString.substring(responseLength - 4);

                //TODO: check status word

                dataBuilder.append(responseData);
            }
            dataBuilder.append(statusWord);
            responses.add(dataBuilder.toString());
        }

        return responses;
    }

    private String getDataFromApdu(final String apdu) {
        if (apdu.length() < 10) {
            return "";
        } else {
            return apdu.substring(10);
        }
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        close();
    }
}