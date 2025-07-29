/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.request;

import androidx.annotation.NonNull;

import com.gsma.sgp.messages.rspdefinitions.CancelSessionRequestEs9;
import com.gsma.sgp.messages.rspdefinitions.CancelSessionResponse;
import com.gsma.sgp.messages.rspdefinitions.TransactionId;
import com.infineon.esim.lpa.core.es9plus.messages.request.base.RequestMsgBody;
import com.infineon.esim.messages.Ber;
import com.infineon.esim.util.Bytes;

public class CancelSessionReq extends RequestMsgBody {
    private String transactionId;
    private String cancelSessionResponse;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getCancelSessionResponse() {
        return cancelSessionResponse;
    }

    public void setCancelSessionResponse(String cancelSessionResponse) {
        this.cancelSessionResponse = cancelSessionResponse;
    }

    public CancelSessionRequestEs9 getRequest() {
        CancelSessionRequestEs9 cancelSessionRequestEs9 = new CancelSessionRequestEs9();

        cancelSessionRequestEs9.setTransactionId(this.getTransactionIdParsed());
        cancelSessionRequestEs9.setCancelSessionResponse(this.getCancelSessionResponseParsed());

        return cancelSessionRequestEs9;
    }

    public void setRequest(CancelSessionRequestEs9 cancelSessionRequestEs9) {
        setTransactionIdParsed(cancelSessionRequestEs9.getTransactionId());
        setCancelSessionResponseParsed(cancelSessionRequestEs9.getCancelSessionResponse());
    }

    private TransactionId getTransactionIdParsed() {
        return new TransactionId(Bytes.decodeHexString(this.transactionId));
    }

    private void setTransactionIdParsed(TransactionId transactionIdParsed) {
        transactionId = Ber.getEncodedValueAsHexString(transactionIdParsed);
    }

    private CancelSessionResponse getCancelSessionResponseParsed() {
        return Ber.createFromEncodedBase64String(CancelSessionResponse.class, cancelSessionResponse);
    }

    private void setCancelSessionResponseParsed(CancelSessionResponse cancelSessionResponse) {
        this.cancelSessionResponse = Ber.getEncodedAsBase64String(cancelSessionResponse);
    }

    @NonNull
    @Override
    public String toString() {
        return "CancelSessionReq{" +
                "transactionId='" + transactionId + '\'' +
                ", cancelSessionResponse='" + cancelSessionResponse + '\'' +
                '}';
    }
}
