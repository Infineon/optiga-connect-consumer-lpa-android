/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.remote;

import com.infineon.esim.lpa.core.dtos.result.GenericOperationResult;

public class DownloadResult extends GenericOperationResult {

    public DownloadResult() {
        super();
    }

    public DownloadResult(RemoteError remoteError) {
        super(remoteError);
    }

    public DownloadResult(GenericOperationResult genericOperationResult) {
        super(genericOperationResult);
    }
}
