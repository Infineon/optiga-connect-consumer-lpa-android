/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa.task;

import com.infineon.esim.lpa.core.dtos.profile.ProfileList;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.lpa.LocalProfileAssistant;
import com.infineon.esim.util.Log;

import java.util.List;
import java.util.concurrent.Callable;

public class GetProfileListTask implements Callable<ProfileList> {
    private static final String TAG = GetProfileListTask.class.getName();

    private final LocalProfileAssistant lpa;

    public GetProfileListTask(LocalProfileAssistant lpa) {
        this.lpa = lpa;
    }

    @Override
    public ProfileList call() throws Exception {
        List<ProfileMetadata> profileMetadataList;
        try {
            profileMetadataList = lpa.getProfiles();
        } catch (Exception e) {
            Log.error(TAG,"Error during getting of profiles.", e);
            throw new Exception("Error during getting of profiles.", e);
        }

        return new ProfileList(profileMetadataList);
    }
}