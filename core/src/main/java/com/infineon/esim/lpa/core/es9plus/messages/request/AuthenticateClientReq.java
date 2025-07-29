/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.request;

import androidx.annotation.NonNull;

import com.gsma.sgp.messages.rspdefinitions.AuthenticateClientRequest;
import com.gsma.sgp.messages.rspdefinitions.AuthenticateServerResponse;
import com.gsma.sgp.messages.rspdefinitions.TransactionId;
import com.infineon.esim.lpa.core.es9plus.messages.request.base.RequestMsgBody;
import com.infineon.esim.messages.Ber;
import com.infineon.esim.util.Bytes;

public class AuthenticateClientReq extends RequestMsgBody {
    private String transactionId;
    private String authenticateServerResponse;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getAuthenticateServerResponse() {
        return authenticateServerResponse;
    }

    public void setAuthenticateServerResponse(String authenticateServerResponse) {
        this.authenticateServerResponse = authenticateServerResponse;
    }

    public AuthenticateClientRequest getRequest() {
        AuthenticateClientRequest authenticateClientRequest = new AuthenticateClientRequest();

        authenticateClientRequest.setTransactionId(this.getTransactionIdParsed());
        authenticateClientRequest.setAuthenticateServerResponse(this.getAuthenticateServerResponseParsed());

        return authenticateClientRequest;
    }

    public void setRequest(AuthenticateClientRequest authenticateClientRequest) {
        setTransactionIdParsed(authenticateClientRequest.getTransactionId());
        setAuthenticateServerResponseParsed(authenticateClientRequest.getAuthenticateServerResponse());
    }

    private TransactionId getTransactionIdParsed() {
        return new TransactionId(Bytes.decodeHexString(this.transactionId));
    }

    private void setTransactionIdParsed(TransactionId transactionIdParsed) {
        transactionId = Ber.getEncodedValueAsHexString(transactionIdParsed);
    }

    private AuthenticateServerResponse getAuthenticateServerResponseParsed() {
        return Ber.createFromEncodedBase64String(AuthenticateServerResponse.class, authenticateServerResponse);
    }

    private void setAuthenticateServerResponseParsed(AuthenticateServerResponse authenticateServerResponseParsed) {
        authenticateServerResponse = Ber.getEncodedAsBase64String(authenticateServerResponseParsed);
    }

    @NonNull
    @Override
    public String toString() {
        return "AuthenticateClientReq{" +
                "transactionId='" + transactionId + '\'' +
                ", authenticateServerResponse='" + authenticateServerResponse + '\'' +
                '}';
    }
}
