/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.scanBarcode;

import androidx.lifecycle.ViewModel;

import com.infineon.esim.lpa.data.DataModel;

public class ScanBarcodeViewModel extends ViewModel {

    private final DataModel dataModel;

    public ScanBarcodeViewModel() {
        this.dataModel = DataModel.getInstance();
    }

    public String getEuiccName() {
        return dataModel.getCurrentEuiccLiveData().getValue();
    }
}
