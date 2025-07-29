/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.downloadProfile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.infineon.esim.lpa.core.dtos.ActivationCode;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.core.dtos.result.remote.AuthenticateResult;
import com.infineon.esim.lpa.core.dtos.result.remote.CancelSessionResult;
import com.infineon.esim.lpa.core.dtos.result.remote.DownloadResult;
import com.infineon.esim.lpa.data.DataModel;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;
import com.infineon.esim.lpa.ui.generic.Error;
import com.infineon.esim.lpa.util.android.OneTimeEvent;

public class DownloadViewModel extends ViewModel {
    private final DataModel dataModel;

    public DownloadViewModel() {
        this.dataModel = DataModel.getInstance();
    }

    public LiveData<OneTimeEvent<Error>> getError() {
        return dataModel.getErrorEventLiveData();
    }

    public LiveData<AsyncActionStatus> getActionStatus() {
        return dataModel.getAsyncActionStatusLiveData();
    }

    public void authenticate(ActivationCode activationCode) {
        dataModel.authenticate(activationCode);
    }

    public void downloadProfile(String confirmationCode) {
        dataModel.downloadProfile(confirmationCode);
    }

    public void cancelSession(long cancelSessionReason) {
        dataModel.cancelSession(cancelSessionReason);
    }

    public String getEuiccName() {
        return dataModel.getCurrentEuiccLiveData().getValue();
    }

    public AuthenticateResult getAuthenticateDownloadResult() {
        return dataModel.getAuthenticateResult();
    }

    public DownloadResult getDownloadResult() {
        return dataModel.getDownloadResult();
    }

    public CancelSessionResult getCancelSessionResult() {
        return dataModel.getCancelSessionResult();
    }

    public void enableProfile(ProfileMetadata profileMetadata) {
        dataModel.enableProfile( profileMetadata);
    }
}
