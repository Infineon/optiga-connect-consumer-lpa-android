/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.profileList;

import androidx.lifecycle.LiveData;
import androidx.test.espresso.IdlingResource;

import com.infineon.esim.lpa.data.DataModel;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;

public class EsimIdlingResource implements IdlingResource {

    private final LiveData<AsyncActionStatus> asyncActionStatusLiveData;

    private ResourceCallback resourceCallback;

    public EsimIdlingResource(ProfileListActivity profileListActivity) {
        DataModel dataModel = DataModel.getInstance();
        asyncActionStatusLiveData = dataModel.getAsyncActionStatusLiveData();
    }

    @Override
    public String getName() {
        return EsimIdlingResource.class.getName();
    }

    @Override
    public boolean isIdleNow() {
        AsyncActionStatus asyncActionStatus = asyncActionStatusLiveData.getValue();

        if((asyncActionStatus == null) || asyncActionStatus.isBusy()) {
            return false;
        } else {
            if (resourceCallback != null) {
                resourceCallback.onTransitionToIdle();
            }
            return true;
        }
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback resourceCallback) {
        this.resourceCallback = resourceCallback;
    }
}
