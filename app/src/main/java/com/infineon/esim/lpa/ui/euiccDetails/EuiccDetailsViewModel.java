/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.euiccDetails;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.infineon.esim.lpa.core.dtos.EuiccInfo;
import com.infineon.esim.lpa.data.DataModel;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;

public class EuiccDetailsViewModel extends ViewModel {
    private final DataModel dataModel;

    public EuiccDetailsViewModel() {
        this.dataModel = DataModel.getInstance();
    }

    public LiveData<AsyncActionStatus> getActionStatus() {
        return dataModel.getAsyncActionStatusLiveData();
    }

    public EuiccInfo getEuiccInfo() {
        return dataModel.getEuiccInfo();
    }

    public void refreshEuiccInfo() {
        dataModel.refreshEuiccInfo();
    }
}
