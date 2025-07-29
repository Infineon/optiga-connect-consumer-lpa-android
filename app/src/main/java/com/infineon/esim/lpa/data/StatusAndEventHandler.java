/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.data;

import com.infineon.esim.lpa.ui.generic.ActionStatus;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;
import com.infineon.esim.lpa.ui.generic.Error;

public interface StatusAndEventHandler {

    void onStatusChange(AsyncActionStatus asyncActionStatus);
    void onStatusChange(ActionStatus actionStatus);
    void onError(Error error);
}
