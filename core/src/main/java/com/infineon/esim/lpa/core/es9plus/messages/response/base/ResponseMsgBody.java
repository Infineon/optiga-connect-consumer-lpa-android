/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.response.base;

import com.infineon.esim.lpa.core.es9plus.messages.Es9plusHttpMsgBody;

public abstract class ResponseMsgBody implements Es9plusHttpMsgBody {
    private Header header;

    public ResponseMsgBody() {
    }

    public Header getHeader() {
        return header;
    }

    public void setHeader(Header header) {
        this.header = header;
    }
}
