/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.remote;

import com.infineon.esim.lpa.core.dtos.result.GenericOperationResult;

public class CancelSessionResult extends GenericOperationResult {

    public CancelSessionResult() {
        super();
    }

    public CancelSessionResult(GenericOperationResult genericOperationResult) {
        super(genericOperationResult);
    }

    public CancelSessionResult(String genericError) {
        super(genericError);

    }

    public CancelSessionResult(RemoteError remoteError) {
        super(remoteError);
    }
}
