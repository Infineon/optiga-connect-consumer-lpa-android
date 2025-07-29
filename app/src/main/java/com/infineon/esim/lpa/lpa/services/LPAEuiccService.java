/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa.services;

import android.service.euicc.EuiccService;
import android.telephony.euicc.EuiccInfo;

import android.service.euicc.GetDefaultDownloadableSubscriptionListResult;
import android.service.euicc.GetDownloadableSubscriptionMetadataResult;
import android.service.euicc.GetEuiccProfileInfoListResult;
import android.telephony.euicc.DownloadableSubscription;

import com.infineon.esim.util.Log;

import com.infineon.esim.lpa.euicc.EuiccManager;

public class LPAEuiccService extends EuiccService {

    private static final String TAG = EuiccManager.class.getName();
    public LPAEuiccService() {
        Log.debug(TAG, "Se service started.");
    }

    @Override
    public String onGetEid(int slotId) {
        Log.debug(TAG, "onGetEid()");

        return "Not implemented.";
    }

    @Override
    public int onGetOtaStatus(int slotId) {

        Log.debug(TAG, "onGetOtaStatus");
        return 5; // EUICC_OTA_STATUS_UNAVAILABLE
    }

    @Override
    public void onStartOtaIfNecessary(int slotId, OtaStatusChangedCallback statusChangedCallback) {
        Log.debug(TAG, "onStartOtaIfNecessary");
        // not implemented
    }

    @Override
    public GetDownloadableSubscriptionMetadataResult onGetDownloadableSubscriptionMetadata(int slotId, DownloadableSubscription subscription, boolean forceDeactivateSim) {

        GetDownloadableSubscriptionMetadataResult result = null;

        result = new GetDownloadableSubscriptionMetadataResult(RESULT_OK, subscription);
        return result;
    }

    @Override
    public GetDefaultDownloadableSubscriptionListResult onGetDefaultDownloadableSubscriptionList(int slotId, boolean forceDeactivateSim) {
        GetDefaultDownloadableSubscriptionListResult result = null;
        return result;
    }

    @Override
    public GetEuiccProfileInfoListResult onGetEuiccProfileInfoList(int slotId) {
        return null;
    }

    @Override
    public EuiccInfo onGetEuiccInfo(int slotId) {
        return new EuiccInfo("Unkown");
    }

    @Override
    public int onDeleteSubscription(int slotId, String iccid) {
        return 0;
    }

    @Override
    public int onSwitchToSubscription(int slotId, String iccid, boolean forceDeactivateSim) {
        return 0;
    }

    @Override
    public int onUpdateSubscriptionNickname(int slotId, String iccid, String nickname) {
        return 0;
    }

    @Override
    public int onEraseSubscriptions(int slotId) {
        return 0;
    }

    @Override
    public int onRetainSubscriptionsForFactoryReset(int slotId) {
        return 0;
    }
}
