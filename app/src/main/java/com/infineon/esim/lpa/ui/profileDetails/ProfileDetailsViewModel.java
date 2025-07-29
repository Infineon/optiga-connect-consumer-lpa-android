/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.profileDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.data.DataModel;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;
import com.infineon.esim.lpa.ui.generic.Error;
import com.infineon.esim.lpa.util.android.OneTimeEvent;

public class ProfileDetailsViewModel extends ViewModel {
    private final DataModel dataModel;

    public ProfileDetailsViewModel() {
        this.dataModel = DataModel.getInstance();
    }

    public LiveData<AsyncActionStatus> getActionStatus() {
        return dataModel.getAsyncActionStatusLiveData();
    }

    public LiveData<OneTimeEvent<Error>> getErrorEvent() {
        return dataModel.getErrorEventLiveData();
    }

    public void enableProfile(ProfileMetadata profile){
        dataModel.enableProfile(profile);
    }

    public void disableProfile(ProfileMetadata profile){
        dataModel.disableProfile(profile);
    }

    public void setNickname(ProfileMetadata profileMetadata) {
        dataModel.setNickname(profileMetadata);
    }
}
