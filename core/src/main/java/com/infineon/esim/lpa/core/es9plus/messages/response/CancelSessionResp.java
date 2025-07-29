/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.response;

import androidx.annotation.NonNull;

import com.gsma.sgp.messages.rspdefinitions.CancelSessionOk;
import com.gsma.sgp.messages.rspdefinitions.CancelSessionResponseEs9;
import com.infineon.esim.lpa.core.es9plus.messages.response.base.ResponseMsgBody;

public class CancelSessionResp extends ResponseMsgBody {
    // Intentionally blank

    @NonNull
    @Override
    public String toString() {
        return "CancelSessionResp{" +
                "header='" + super.getHeader() + '\'' +
                "}";
    }

    public CancelSessionResponseEs9 getResponse() {
        CancelSessionOk cancelSessionOk = new CancelSessionOk();

        CancelSessionResponseEs9 cancelSessionResponseEs9 = new CancelSessionResponseEs9();
        cancelSessionResponseEs9.setCancelSessionOk(cancelSessionOk);

        return cancelSessionResponseEs9;
    }
}
