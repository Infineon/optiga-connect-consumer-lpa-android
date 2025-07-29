/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa.task;

import com.infineon.esim.lpa.core.dtos.enums.ProfileActionType;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.core.dtos.result.local.ConfigureMEPResult;
import com.infineon.esim.lpa.core.dtos.result.local.EnableResult;
import com.infineon.esim.lpa.core.dtos.result.local.OperationResult;
import com.infineon.esim.lpa.core.dtos.result.remote.HandleNotificationsResult;
import com.infineon.esim.lpa.lpa.LocalProfileAssistant;
import com.infineon.esim.util.Log;

import java.net.ConnectException;
import java.util.concurrent.Callable;

public class ProfileActionTask implements Callable<Void> {
    private static final String TAG = ProfileActionTask.class.getName();

    private final LocalProfileAssistant lpa;
    private final ProfileActionType profileActionType;
    private final ProfileMetadata profile;

    private OperationResult profileOperationResult = null;

    public ProfileActionTask(LocalProfileAssistant lpa,
                             ProfileActionType profileActionType,
                             ProfileMetadata profile) {
        this.lpa = lpa;
        this.profileActionType = profileActionType;
        this.profile = profile;
    }

    @Override
    public Void call() throws Exception {

        int result = lpa.configureMEP(profile.getIccid(), profileActionType);
        profileOperationResult = new ConfigureMEPResult(result);

        if(profileOperationResult.isOk()) {
            switch (profileActionType) {
                case PROFILE_ACTION_ENABLE:
                    profileOperationResult = lpa.enableProfile(profile.getIccid(), true);
                    break;
                case PROFILE_ACTION_DISABLE:
                    profileOperationResult = lpa.disableProfile(profile.getIccid());
                    break;
                case PROFILE_ACTION_DELETE:
                    // If profile is currently enabled, disable it first
                    if (profile.isEnabled()) {
                        Log.info(TAG,"Profile that shall be deleted is enabled. First disable it.");
                        profileOperationResult = lpa.disableProfile(profile.getIccid());

                        handleProfileOperationResult(profileOperationResult);
                        performEuiccReset();
                    }
                    Log.info(TAG,"Deleting the profile.");
                    profileOperationResult = lpa.deleteProfile(profile.getIccid());

                    break;
                case PROFILE_ACTION_SET_NICKNAME:
                    profileOperationResult = lpa.setNickname(profile.getIccid(), profile.getNickname());
                    break;
            }
        }

        // Handle profile operation result
        handleProfileOperationResult(profileOperationResult);

        performEuiccReset();

        // Send notification
        try {
            HandleNotificationsResult handleNotificationsResult = lpa.handleNotifications();
            if(!handleNotificationsResult.getSuccess()) {
                Log.error(TAG, "HandleNotifications failed: " + handleNotificationsResult.getErrorDetails());
                throw new Exception("HandleNotifications failed: " + handleNotificationsResult.getErrorDetails());
            }
        } catch (ConnectException e) {
            // Ignore exceptions (E.g. no internet connection) and retry later
            // TODO: Show toast on fail?
        }

        return null;
    }

    private void performEuiccReset() throws Exception {
        Log.debug(TAG, "Performing eUICC reset.");
        switch (profileActionType) {
            case PROFILE_ACTION_ENABLE:
            case PROFILE_ACTION_DISABLE:
            case PROFILE_ACTION_DELETE:
                if (!lpa.resetEuicc()) {
                    Log.error(TAG, "Resetting eUICC failed.");
                    throw new Exception("Resetting eUICC failed.");
                }
                break;
            case PROFILE_ACTION_SET_NICKNAME:
                // do nothing
                break;
        }
    }

    private void handleProfileOperationResult(OperationResult operationResult) {
        switch (profileActionType) {
            case PROFILE_ACTION_ENABLE:
                if(operationResult.equals(EnableResult.NONE)) {
                    // suppress errors during enable because there is a situation
                    // where the phone's radio reset after a new profile is enabled,
                    // which causes OMAPI channel.transmit() to fail
                    Log.error(TAG,"Ignoring profile operation error after enable.");
                    return;
                }
            case PROFILE_ACTION_DISABLE:
            case PROFILE_ACTION_DELETE:
            case PROFILE_ACTION_SET_NICKNAME:
                if(profileOperationResult.isOk()) {
                    Log.debug(TAG, "Profile operation successful!");
                } else {
                    Log.debug(TAG,"Profile operation failed: " + profileOperationResult.getDescription());
                    throw new RuntimeException("Profile operation failed: " + profileOperationResult.getDescription());
                }
                break;
        }
    }
}