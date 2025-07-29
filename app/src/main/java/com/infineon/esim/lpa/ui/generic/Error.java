/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.generic;

import androidx.annotation.NonNull;

public class Error {
    private final String header;
    private final String body;

    public Error(String header, String body) {
        this.header = header;
        this.body = body;
    }

    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    @Override
    @NonNull
    public String toString() {
        return "Error{" +
                "header='" + header + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}
