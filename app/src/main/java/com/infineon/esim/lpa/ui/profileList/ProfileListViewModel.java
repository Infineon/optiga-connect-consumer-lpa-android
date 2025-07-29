/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.profileList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.infineon.esim.lpa.core.dtos.profile.ProfileList;
import com.infineon.esim.lpa.data.DataModel;
import com.infineon.esim.lpa.euicc.identive.IdentiveConnectionBroadcastReceiver;
import com.infineon.esim.lpa.euicc.identive.IdentiveEuiccInterface;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;
import com.infineon.esim.lpa.ui.generic.Error;
import com.infineon.esim.lpa.util.android.OneTimeEvent;
import com.infineon.esim.util.Log;

public class ProfileListViewModel extends ViewModel {
    private static final String TAG = ProfileListViewModel.class.getName();

    private final DataModel dataModel;

    public ProfileListViewModel() {
        this.dataModel = DataModel.getInstance();
    }

    public void selectFreshlyAttachedUsbReader() {
        // Check if USB reader really has been freshly attached
        try {
            if (IdentiveConnectionBroadcastReceiver.hasBeenFreshlyAttached()) {
                Log.debug(TAG, "USB reader is freshly attached.");
                connectIdentiveEuiccInterface();
            }
        } catch (Exception e) {
            dataModel.onError(new Error("Exception during switching to freshly attached USB reader.", e.getMessage()));
        }
    }

    public void connectIdentiveEuiccInterface() {
        Log.debug(TAG,"Connecting Identive eUICC interface...");
        dataModel.startConnectingEuiccInterface(IdentiveEuiccInterface.INTERFACE_TAG);
    }

    public LiveData<String> getEuiccNameLiveData() {
        return dataModel.getCurrentEuiccLiveData();
    }

    public LiveData<ProfileList> getProfileListLiveData() {
        return dataModel.getProfileListLiveData();
    }

    public LiveData<AsyncActionStatus> getActionStatusLiveData() {
        return dataModel.getAsyncActionStatusLiveData();
    }

    public LiveData<OneTimeEvent<Error>> getError() {
        return dataModel.getErrorEventLiveData();
    }

    public void refreshProfileList() {
        dataModel.refreshProfileList();
    }

    public void refreshEuiccs() {
        dataModel.refreshEuiccs();
    }

    public void clearAllNotifications() {
        dataModel.handleAndClearAllNotifications();
    }

}
