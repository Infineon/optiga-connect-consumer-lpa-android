/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.response.base;

import androidx.annotation.NonNull;

public class Header {
    private FunctionExecutionStatus functionExecutionStatus;

    public Header() {
    }

    public Header(FunctionExecutionStatus functionExecutionStatus) {
        super();
        this.functionExecutionStatus = functionExecutionStatus;
    }

    public FunctionExecutionStatus getFunctionExecutionStatus() {
        return functionExecutionStatus;
    }

    public void setFunctionExecutionStatus(FunctionExecutionStatus functionExecutionStatus) {
        this.functionExecutionStatus = functionExecutionStatus;
    }

    @NonNull
    @Override
    public String toString() {
        return "Header{" +
                "functionExecutionStatus=" + functionExecutionStatus +
                '}';
    }
}
