/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.response.base;


import androidx.annotation.NonNull;

public class StatusCodeData {

    private String subjectCode;
    private String reasonCode;
    private String message;

    public StatusCodeData() {
    }

    public StatusCodeData(String subjectCode, String reasonCode, String message) {
        this.subjectCode = subjectCode;
        this.reasonCode = reasonCode;
        this.message = message;
    }

    public StatusCodeData(String subjectCode, String reasonCode, String message, String subjectIdentifier) {
        this.subjectCode = subjectCode;
        this.reasonCode = reasonCode;
        this.message = message;
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

    @NonNull
    @Override
    public String toString() {
        return "StatusCodeData{" +
                "subjectCode='" + subjectCode + '\'' +
                ", reasonCode='" + reasonCode + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
