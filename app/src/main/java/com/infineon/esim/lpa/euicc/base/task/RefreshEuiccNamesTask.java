/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.base.task;

import com.infineon.esim.lpa.euicc.base.EuiccInterface;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

public class RefreshEuiccNamesTask implements Callable<List<String>> {
    private static final String TAG = RefreshEuiccNamesTask.class.getName();

    private final List<EuiccInterface> euiccInterfaces;

    public RefreshEuiccNamesTask(List<EuiccInterface> euiccInterfaces) {
        this.euiccInterfaces = euiccInterfaces;
    }

    @Override
    public List<String> call() throws Exception {
        Log.debug(TAG,"Refreshing connected eUICC names.");

        List<String> euiccList = new ArrayList<>();
        for(EuiccInterface euiccInterface : euiccInterfaces) {
            for (String euiccName : euiccInterface.refreshEuiccNames()) {
                Log.debug(TAG, euiccInterface.getTag() + ": " + euiccName);
                euiccList.add(euiccName);
            }
        }

        return euiccList;
    }
}
