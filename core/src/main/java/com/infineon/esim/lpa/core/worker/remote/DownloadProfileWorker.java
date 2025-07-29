/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.remote;

import com.gsma.sgp.messages.rspdefinitions.GetBoundProfilePackageRequest;
import com.gsma.sgp.messages.rspdefinitions.GetBoundProfilePackageResponse;
import com.gsma.sgp.messages.rspdefinitions.Octet32;
import com.gsma.sgp.messages.rspdefinitions.PrepareDownloadRequest;
import com.gsma.sgp.messages.rspdefinitions.PrepareDownloadResponse;
import com.gsma.sgp.messages.rspdefinitions.ProfileInstallationResult;
import com.infineon.esim.lpa.core.dtos.ConfirmationCode;
import com.infineon.esim.lpa.core.dtos.ProfileDownloadSession;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.lpa.core.es9plus.Es9PlusInterface;
import com.infineon.esim.util.Log;

public class DownloadProfileWorker {
    private static final String TAG = DownloadProfileWorker.class.getName();

    private final ProfileDownloadSession profileDownloadSession;

    private final Es10Interface es10Interface;
    private final Es9PlusInterface es9PlusInterface;

    public DownloadProfileWorker(ProfileDownloadSession profileDownloadSession) {
        this.profileDownloadSession = profileDownloadSession;
        this.es10Interface = profileDownloadSession.getEs10Interface();
        this.es9PlusInterface = profileDownloadSession.getEs9PlusInterface();
    }

    public boolean downloadProfile(String confirmationCode) throws Exception {
        Log.debug(TAG, "Downloading profile...");

        // Process confirmation code hash (optional)
        Octet32 hashCc = null;
        if(confirmationCode != null) {
            hashCc = ConfirmationCode.getHashCC(confirmationCode, profileDownloadSession.getTransactionId());
        }

        // Send PrepareDownloadRequest to eUICC
        PrepareDownloadRequest prepareDownloadRequest = profileDownloadSession.es10_getPrepareDownloadRequest(hashCc);
        PrepareDownloadResponse prepareDownloadResponse = es10Interface.es10b_prepareDownloadRequest(prepareDownloadRequest);
        profileDownloadSession.es10_processPrepareDownloadResponse(prepareDownloadResponse);

        // Send GetBoundProfilePackageRequest to SM-DP+
        GetBoundProfilePackageRequest getBoundProfilePackageRequest = profileDownloadSession.es9Plus_getBoundProfilePackageRequest();
        GetBoundProfilePackageResponse getBoundProfilePackageResponse = es9PlusInterface.getBoundProfilePackage(getBoundProfilePackageRequest);
        profileDownloadSession.es9Plus_processGetBoundProfilePackageResponse(es9PlusInterface.getFunctionExecutionStatus(), getBoundProfilePackageResponse);

        // Send LoadBoundProfilePackage to eUICC
        ProfileInstallationResult profileInstallationResult = es10Interface.es10b_loadBoundProfilePackage(profileDownloadSession.es10_getBoundProfilePackage());
        profileDownloadSession.es10_processProfileInstallationResult(profileInstallationResult);

        return profileDownloadSession.isProfileInstalledSuccessfully();
    }
}
