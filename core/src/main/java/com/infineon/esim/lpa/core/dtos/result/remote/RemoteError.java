/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.remote;

import androidx.annotation.NonNull;

import com.infineon.esim.lpa.core.es9plus.messages.response.base.FunctionExecutionStatus;

public class RemoteError {
    private final String status;
    private final String subjectCode;
    private final String reasonCode;
    private final String message;

    public RemoteError() {
        this("No error",null, null, null);
    }

    public RemoteError(FunctionExecutionStatus functionExecutionStatus) {
        if(functionExecutionStatus == null) {
            this.status = "No error";
            this.subjectCode = null;
            this.reasonCode = null;
            this.message = null;
        } else {
            this.status = functionExecutionStatus.getStatus();
            if(functionExecutionStatus.getStatusCodeData() != null) {
                this.subjectCode = functionExecutionStatus.getStatusCodeData().getSubjectCode();
                this.reasonCode = functionExecutionStatus.getStatusCodeData().getReasonCode();
                this.message = functionExecutionStatus.getStatusCodeData().getMessage();
            } else {
                this.subjectCode = null;
                this.reasonCode = null;
                this.message = null;
            }
        }
    }

    public RemoteError(String status, String subjectCode, String reasonCode, String message) {
        this.status = status;
        this.subjectCode = subjectCode;
        this.reasonCode = reasonCode;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public String getSubjectCode() {
        return subjectCode;
    }

    public String getReasonCode() {
        return reasonCode;
    }

    public String getMessage() {
        return message;
    }

    @Override
    @NonNull
    public String toString() {
        return "RspError{" +
                "status='" + status + '\'' +
                ", subjectCode='" + subjectCode + '\'' +
                ", reasonCode='" + reasonCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
