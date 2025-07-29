/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.remote;

import com.gsma.sgp.messages.rspdefinitions.CancelSessionReason;
import com.gsma.sgp.messages.rspdefinitions.CancelSessionRequest;
import com.gsma.sgp.messages.rspdefinitions.CancelSessionRequestEs9;
import com.gsma.sgp.messages.rspdefinitions.CancelSessionResponse;
import com.gsma.sgp.messages.rspdefinitions.CancelSessionResponseEs9;
import com.infineon.esim.lpa.core.dtos.ProfileDownloadSession;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.lpa.core.es9plus.Es9PlusInterface;
import com.infineon.esim.util.Log;

public class CancelSessionWorker {
    private static final String TAG = CancelSessionWorker.class.getName();

    private final ProfileDownloadSession profileDownloadSession;

    private final Es10Interface es10Interface;
    private final Es9PlusInterface es9PlusInterface;

    public CancelSessionWorker(ProfileDownloadSession profileDownloadSession) {
        this.profileDownloadSession = profileDownloadSession;
        this.es10Interface = profileDownloadSession.getEs10Interface();
        this.es9PlusInterface = profileDownloadSession.getEs9PlusInterface();
    }

    public boolean cancelSession(long cancelSessionReasonValue) throws Exception {
        CancelSessionReason cancelSessionReason = new CancelSessionReason(cancelSessionReasonValue);
        Log.debug(TAG, "Cancelling  session with cancel session reason " + cancelSessionReason + "...");

        // Send CancelSession to eUICC
        CancelSessionRequest cancelSessionRequest = profileDownloadSession.es10_getCancelSessionRequest(cancelSessionReason);
        CancelSessionResponse cancelSessionResponse = es10Interface.es10b_cancelSession(cancelSessionRequest);
        profileDownloadSession.es10_processCancelSessionResponse(cancelSessionResponse);

        // Send CancelSession to SM-DP+
        CancelSessionRequestEs9 cancelSessionRequestEs9 = profileDownloadSession.es9Plus_getCancelSessionRequest();
        CancelSessionResponseEs9 cancelSessionResponseEs9 = es9PlusInterface.cancelSession(cancelSessionRequestEs9);
        profileDownloadSession.es9Plus_processCancelSessionResponse(cancelSessionResponseEs9);

        return profileDownloadSession.isProfileSessionCancelledSuccessfully();
    }
}
