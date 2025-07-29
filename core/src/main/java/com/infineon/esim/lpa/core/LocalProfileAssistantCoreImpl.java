/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core;

import com.infineon.esim.lpa.core.dtos.ActivationCode;
import com.infineon.esim.lpa.core.dtos.EuiccInfo;
import com.infineon.esim.lpa.core.dtos.ProfileDownloadSession;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.core.dtos.result.GenericOperationResult;
import com.infineon.esim.lpa.core.dtos.result.local.ClearNotificationsResult;
import com.infineon.esim.lpa.core.dtos.result.local.DeleteResult;
import com.infineon.esim.lpa.core.dtos.result.local.DisableResult;
import com.infineon.esim.lpa.core.dtos.result.local.EnableResult;
import com.infineon.esim.lpa.core.dtos.result.local.SetNicknameResult;
import com.infineon.esim.lpa.core.dtos.result.remote.AuthenticateResult;
import com.infineon.esim.lpa.core.dtos.result.remote.CancelSessionResult;
import com.infineon.esim.lpa.core.dtos.result.remote.DownloadResult;
import com.infineon.esim.lpa.core.dtos.result.remote.HandleNotificationsResult;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.lpa.core.es10.EuiccChannel;
import com.infineon.esim.lpa.core.es9plus.Es9PlusInterface;
import com.infineon.esim.lpa.core.worker.local.ClearAllNotificationsWorker;
import com.infineon.esim.lpa.core.worker.local.DeleteProfileWorker;
import com.infineon.esim.lpa.core.worker.local.DisableProfileWorker;
import com.infineon.esim.lpa.core.worker.local.EnableProfileWorker;
import com.infineon.esim.lpa.core.worker.local.GetEidWorker;
import com.infineon.esim.lpa.core.worker.local.GetEuiccInfo2Worker;
import com.infineon.esim.lpa.core.worker.local.ListProfilesWorker;
import com.infineon.esim.lpa.core.worker.local.SetNicknameWorker;
import com.infineon.esim.lpa.core.worker.remote.AuthenticateWorker;
import com.infineon.esim.lpa.core.worker.remote.CancelSessionWorker;
import com.infineon.esim.lpa.core.worker.remote.DownloadProfileWorker;
import com.infineon.esim.lpa.core.worker.remote.HandleNotificationsWorker;
import com.infineon.esim.util.Log;

import java.util.List;

public class LocalProfileAssistantCoreImpl implements LocalProfileAssistantCore {
    private static final String TAG = LocalProfileAssistantCoreImpl.class.getName();

    private ProfileDownloadSession profileDownloadSession = null;

    private Es10Interface es10Interface;
    private Es9PlusInterface es9PlusInterface;

    public LocalProfileAssistantCoreImpl() {
    }

    public void setEuiccChannel(EuiccChannel euiccChannel) {
        this.es10Interface = new Es10Interface(euiccChannel);
    }

    public void enableEs9PlusInterface() {
        this.es9PlusInterface = new Es9PlusInterface();
    }

    public void disableEs9PlusInterface() {
        this.es9PlusInterface = null;
    }

    // Local functions

    @Override
    public EnableResult enableProfile(String iccid, boolean refreshFlag) throws Exception {
        int result = new EnableProfileWorker(es10Interface).enable(iccid, refreshFlag);

        return new EnableResult(result);
    }

    @Override
    public DisableResult disableProfile(String iccid) throws Exception {
        int result = new DisableProfileWorker(es10Interface).disable(iccid);

        return new DisableResult(result);
    }

    @Override
    public DeleteResult deleteProfile(String iccid) throws Exception {
        int result = new DeleteProfileWorker(es10Interface).delete(iccid);

        return new DeleteResult(result);
    }

    @Override
    public SetNicknameResult setNickname(String iccid, String nicknameNew) throws Exception {
        int result = new SetNicknameWorker(es10Interface).setNickname(iccid,nicknameNew);

        return new SetNicknameResult(result);
    }

    @Override
    public List<ProfileMetadata> getProfiles() throws Exception {
        return new ListProfilesWorker(es10Interface).listProfiles();
    }

    @Override
    public String getEID() throws Exception {
        return new GetEidWorker(es10Interface).getEid();
    }

    @Override
    public EuiccInfo getEuiccInfo2() throws Exception {
        return new GetEuiccInfo2Worker(es10Interface).getEuiccInfo2();
    }

    // Remote functions

    @Override
    public AuthenticateResult authenticate(ActivationCode activationCode) throws Exception {
        if(isEs9PlusInterfaceUnavailable()) {
            Log.error(TAG, "ES9+ interface is not available! Enable internet connection?");
            throw new Exception("ES9+ interface is not available! Enable internet connection?");
        }

        profileDownloadSession = new ProfileDownloadSession(activationCode, es10Interface, es9PlusInterface);

        boolean success = new AuthenticateWorker(profileDownloadSession).authenticate();

        if(success) {
            ProfileMetadata profileMetadata =  new ProfileMetadata(profileDownloadSession.getProfileMetaData());
            return new AuthenticateResult(profileDownloadSession.isCcRequired(), profileMetadata);
        } else {
            return new AuthenticateResult(getLastError());
        }
    }

    @Override
    public DownloadResult downloadProfile(String confirmationCode) throws Exception {
        if(isEs9PlusInterfaceUnavailable()) {
            Log.error(TAG, "ES9+ interface is not available! Enable internet connection?");
            throw new Exception("ES9+ interface is not available! Enable internet connection?");
        }

        boolean success = new DownloadProfileWorker(profileDownloadSession).downloadProfile(confirmationCode);

        if(success) {
            return new DownloadResult();
        } else {
            return new DownloadResult(getLastError());
        }
    }

    @Override
    public CancelSessionResult cancelSession(long cancelSessionReasonValue) throws Exception {
        if(isEs9PlusInterfaceUnavailable()) {
            Log.error(TAG, "ES9+ interface is not available! Enable internet connection?");
            throw new Exception("ES9+ interface is not available! Enable internet connection?");
        }

        if (profileDownloadSession != null) {
            boolean success = new CancelSessionWorker(profileDownloadSession).cancelSession(cancelSessionReasonValue);

            if(success) {
                return new CancelSessionResult();
            } else {
                return new CancelSessionResult(getLastError());
            }
        }

        return new CancelSessionResult("Error: no profile download session active that can be cancelled.");
    }

    @Override
    public HandleNotificationsResult handleNotifications() throws Exception {
        if(isEs9PlusInterfaceUnavailable()) {
            Log.error(TAG, "ES9+ interface is not available! Enable internet connection?");
            throw new Exception("ES9+ interface is not available! Enable internet connection?");
        }

        boolean success = new HandleNotificationsWorker(es10Interface, es9PlusInterface).handleNotifications();

        if (success) {
            return new HandleNotificationsResult();
        } else {
            return new HandleNotificationsResult(getLastError());
        }
    }

    @Override
    public ClearNotificationsResult clearPendingNotifications() throws Exception {
        Log.debug(TAG,"Now clearing all pending notifications.");
        List<Integer> resultValues = new ClearAllNotificationsWorker(es10Interface).clearAllNotifications();

        return new ClearNotificationsResult(resultValues);
    }

    @Override
    public GenericOperationResult getLastError() {
        return profileDownloadSession.getLastError();
    }


    private boolean isEs9PlusInterfaceUnavailable() {
        return es9PlusInterface == null;
    }
 }
