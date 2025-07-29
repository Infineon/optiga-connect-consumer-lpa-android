/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.remote;

import com.infineon.esim.lpa.core.dtos.result.GenericOperationResult;

public class HandleNotificationsResult extends GenericOperationResult {

    public HandleNotificationsResult() {
        super();
    }

    public HandleNotificationsResult(GenericOperationResult genericOperationResult) {
        super(genericOperationResult);
    }
    public HandleNotificationsResult(RemoteError remoteError) {
        super(remoteError);
    }
}
