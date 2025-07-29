/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.request;

import androidx.annotation.NonNull;

import com.gsma.sgp.messages.rspdefinitions.GetBoundProfilePackageRequest;
import com.gsma.sgp.messages.rspdefinitions.PrepareDownloadResponse;
import com.gsma.sgp.messages.rspdefinitions.TransactionId;
import com.infineon.esim.lpa.core.es9plus.messages.request.base.RequestMsgBody;
import com.infineon.esim.messages.Ber;
import com.infineon.esim.util.Bytes;


public class GetBoundProfilePackageReq extends RequestMsgBody {

    private String transactionId;
    private String prepareDownloadResponse;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getPrepareDownloadResponse() {
        return prepareDownloadResponse;
    }

    public void setPrepareDownloadResponse(String prepareDownloadResponse) {
        this.prepareDownloadResponse = prepareDownloadResponse;
    }

    public GetBoundProfilePackageRequest getRequest() {
        GetBoundProfilePackageRequest getBoundProfilePackageRequest = new GetBoundProfilePackageRequest();

        getBoundProfilePackageRequest.setTransactionId(this.getTransactionIdParsed());
        getBoundProfilePackageRequest.setPrepareDownloadResponse(this.getAuthenticateServerResponseParsed());

        return getBoundProfilePackageRequest;
    }

    public void setRequest(GetBoundProfilePackageRequest getBoundProfilePackageRequest) {
        setTransactionIdParsed(getBoundProfilePackageRequest.getTransactionId());
        setPrepareDownloadResponseParsed(getBoundProfilePackageRequest.getPrepareDownloadResponse());
    }

    private TransactionId getTransactionIdParsed() {
        return new TransactionId(Bytes.decodeHexString(this.transactionId));
    }

    private void setTransactionIdParsed(TransactionId transactionIdParsed) {
        transactionId = Ber.getEncodedValueAsHexString(transactionIdParsed);
    }

    private PrepareDownloadResponse getAuthenticateServerResponseParsed() {
        return Ber.createFromEncodedBase64String(PrepareDownloadResponse.class, prepareDownloadResponse);
    }

    private void setPrepareDownloadResponseParsed(PrepareDownloadResponse prepareDownloadResponseParsed) {
        prepareDownloadResponse = Ber.getEncodedAsBase64String(prepareDownloadResponseParsed);
    }

    @NonNull
    @Override
    public String toString() {
        return "GetBoundProfilePackageReq{" +
                "transactionId='" + transactionId + '\'' +
                ", prepareDownloadResponse='" + prepareDownloadResponse + '\'' +
                '}';
    }
}
