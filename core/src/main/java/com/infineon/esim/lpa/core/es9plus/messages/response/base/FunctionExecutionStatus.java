/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.response.base;

import androidx.annotation.NonNull;

public class FunctionExecutionStatus {
    public static final String EXECUTION_STATUS_SUCCESS = "Executed-Success";
    public static final String EXECUTION_STATUS_WITH_WARNING = "Executed-WithWarning";
    public static final String EXECUTION_STATUS_WITH_FAILED = "Failed";
    public static final String EXECUTION_STATUS_WITH_EXPIRED = "Expired";

    private String status;
    private StatusCodeData statusCodeData;

    public FunctionExecutionStatus() {
    }

    public FunctionExecutionStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StatusCodeData getStatusCodeData() {
        return statusCodeData;
    }

    public void setStatusCodeData(StatusCodeData statusCodeData) {
        this.statusCodeData = statusCodeData;
    }

    public boolean isSuccess() {
        return status.equals(EXECUTION_STATUS_SUCCESS) || status.equals(EXECUTION_STATUS_WITH_WARNING);
    }

    @NonNull
    @Override
    public String toString() {
        return "FunctionExecutionStatus{\n" +
                "  status='" + status + "'\n" +
                "  statusCodeData=" + statusCodeData +
                '}';
    }
}
