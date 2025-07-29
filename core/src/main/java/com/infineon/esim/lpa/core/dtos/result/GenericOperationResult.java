/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result;

import com.infineon.esim.lpa.core.dtos.result.remote.RemoteError;

public class GenericOperationResult {
    private final Boolean success;
    private final String genericError;
    private final RemoteError remoteError;

    public GenericOperationResult() {
        this.success = true;
        this.genericError = null;
        this.remoteError = null;
    }

    public GenericOperationResult(GenericOperationResult genericOperationResult) {
        this.success = genericOperationResult.success;
        this.genericError = genericOperationResult.genericError;
        this.remoteError = genericOperationResult.remoteError;
    }

    public GenericOperationResult(RemoteError remoteError) {
        this.success = false;
        this.genericError = null;
        this.remoteError = remoteError;
    }

    public GenericOperationResult(String genericError) {
        this.success = false;
        this.genericError = genericError;
        this.remoteError = null;
    }

    public Boolean getSuccess() {
        return success;
    }

    public RemoteError getRemoteError() {
        return remoteError;
    }

    public String getErrorDetails() {
        if(genericError != null) {
            return genericError;
        }

        if (remoteError != null) {
            return String.format(
                    "Status: %1$s\nSubject Code: %2$s\nReason Code: %3$s\nMessage: %4$s",
                    remoteError.getStatus(),
                    remoteError.getSubjectCode(),
                    remoteError.getReasonCode(),
                    remoteError.getMessage());
        }

        return null;
    }
}
