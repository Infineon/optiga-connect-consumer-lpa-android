/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.preference;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.infineon.esim.lpa.data.DataModel;
import com.infineon.esim.lpa.data.Preferences;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;
import com.infineon.esim.lpa.ui.generic.Error;
import com.infineon.esim.lpa.util.android.OneTimeEvent;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PreferenceViewModel extends ViewModel {
    private static final String TAG = PreferenceViewModel.class.getName();

    private final DataModel dataModel;

    public PreferenceViewModel() {
        this.dataModel = DataModel.getInstance();
    }

    public LiveData<AsyncActionStatus> getActionStatus() {
        return dataModel.getAsyncActionStatusLiveData();
    }

    public LiveData<OneTimeEvent<Error>> getErrorEvent() {
        return dataModel.getErrorEventLiveData();
    }

    public LiveData<List<String>> getEuiccListLiveData() {
        return dataModel.getEuiccListLiveData();
    }

    public LiveData<String> getCurrentEuiccLiveData() {
        return dataModel.getCurrentEuiccLiveData();
    }

    public List<String> getEuiccList() {
        List<String> euiccList = new ArrayList<>();
        LiveData<List<String>> euiccListLiveData = getEuiccListLiveData();
        if(euiccListLiveData.getValue() != null) {
            euiccList.addAll(euiccListLiveData.getValue());
        }

        return euiccList;
    }

    public String getCurrentEuicc() {
        LiveData<String> currentEuiccLiveData = getCurrentEuiccLiveData();
        return currentEuiccLiveData.getValue();
    }

    public void savePreferences() {
        Log.debug(TAG, "Saving preferences.");
        if(Preferences.havePreferencesChanged()) {
            Log.debug(TAG, "Preferences have changed. Select new eUICC: " + Preferences.getEuiccName());
            dataModel.switchEuicc(Preferences.getEuiccName());
        }
    }
}
