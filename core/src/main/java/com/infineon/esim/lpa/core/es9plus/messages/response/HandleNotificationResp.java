/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.response;

import androidx.annotation.NonNull;

import com.infineon.esim.lpa.core.es9plus.messages.response.base.ResponseMsgBody;

public class HandleNotificationResp extends ResponseMsgBody {
    // Intentionally blank

    @NonNull
    @Override
    public String toString() {
        return "HandleNotificationResp{" +
                "header='" + super.getHeader() + '\'' +
                "}";
    }
}
