/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.worker.local;

import com.gsma.sgp.messages.rspdefinitions.ProfileInfo;
import com.gsma.sgp.messages.rspdefinitions.ProfileInfoListResponse;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.core.es10.Es10Interface;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ListProfilesWorker {
    private static final String TAG = ListProfilesWorker.class.getName();

    private final Es10Interface es10Interface;

    public ListProfilesWorker(Es10Interface es10Interface) {
        this.es10Interface = es10Interface;
    }

    public List<ProfileMetadata> listProfiles() throws Exception {
        Log.debug(TAG, "Getting list of all profiles...");

        ProfileInfoListResponse profileInfoListResponse = es10Interface.es10c_getProfilesInfoAll();

        List<ProfileMetadata> profileMetadataList = new ArrayList<>();

        for (ProfileInfo profileInfo : profileInfoListResponse.getProfileInfoListOk().getProfileInfo()) {
            if(profileInfo.getIccid() != null) {
                profileMetadataList.add(new ProfileMetadata(profileInfo));
            }
        }

        return profileMetadataList;
    }
}
