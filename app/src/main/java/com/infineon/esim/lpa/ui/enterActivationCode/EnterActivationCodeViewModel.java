/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.enterActivationCode;


import androidx.lifecycle.ViewModel;

import com.infineon.esim.lpa.data.DataModel;

public class EnterActivationCodeViewModel extends ViewModel {

    private final DataModel dataModel;

    public EnterActivationCodeViewModel() {
        this.dataModel = DataModel.getInstance();
    }

    public String getEuiccName() {
        return dataModel.getCurrentEuiccLiveData().getValue();
    }
}

