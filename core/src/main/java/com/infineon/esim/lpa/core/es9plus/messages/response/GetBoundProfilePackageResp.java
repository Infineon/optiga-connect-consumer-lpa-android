/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es9plus.messages.response;

import androidx.annotation.NonNull;

import com.gsma.sgp.messages.rspdefinitions.BoundProfilePackage;
import com.gsma.sgp.messages.rspdefinitions.GetBoundProfilePackageOk;
import com.gsma.sgp.messages.rspdefinitions.GetBoundProfilePackageResponse;
import com.gsma.sgp.messages.rspdefinitions.TransactionId;
import com.infineon.esim.lpa.core.es9plus.messages.response.base.ResponseMsgBody;
import com.infineon.esim.messages.Ber;
import com.infineon.esim.util.Bytes;


public class GetBoundProfilePackageResp extends ResponseMsgBody {

    private String transactionId;
    private String boundProfilePackage;

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getBoundProfilePackage() {
        return boundProfilePackage;
    }

    public void setBoundProfilePackage(String boundProfilePackage) {
        this.boundProfilePackage = boundProfilePackage;
    }

    public GetBoundProfilePackageResponse getResponse() {
        GetBoundProfilePackageOk getBoundProfilePackageOk = new GetBoundProfilePackageOk();
        getBoundProfilePackageOk.setTransactionId(this.getTransactionIdParsed());
        getBoundProfilePackageOk.setBoundProfilePackage(this.getBoundProfilePackageParsed());

        GetBoundProfilePackageResponse getBoundProfilePackageResponse = new GetBoundProfilePackageResponse();
        getBoundProfilePackageResponse.setGetBoundProfilePackageOk(getBoundProfilePackageOk);

        return getBoundProfilePackageResponse;
    }

    public void setResponse(GetBoundProfilePackageResponse getBoundProfilePackageResponse) {
        TransactionId transactionID = getBoundProfilePackageResponse.getGetBoundProfilePackageOk().getTransactionId();
        BoundProfilePackage boundProfilePackage = getBoundProfilePackageResponse.getGetBoundProfilePackageOk().getBoundProfilePackage();

        this.transactionId = this.getEncodedTransactionId(transactionID);
        this.boundProfilePackage = this.getEncodedBoundProfilePackage(boundProfilePackage);
    }

    public TransactionId getTransactionIdParsed() {
        return new TransactionId(Bytes.decodeHexString(this.transactionId));
    }

    public BoundProfilePackage getBoundProfilePackageParsed() {
        return Ber.createFromEncodedBase64String(BoundProfilePackage.class, boundProfilePackage);
    }

    private String getEncodedTransactionId(TransactionId transactionId) {
        return Ber.getEncodedValueAsHexString(transactionId);
    }

    private String getEncodedBoundProfilePackage(BoundProfilePackage boundProfilePackage) {
        return Ber.getEncodedAsBase64String(boundProfilePackage);
    }

    @NonNull
    @Override
    public String toString() {
        return "GetBoundProfilePackageResp{" +
                "header='" + super.getHeader() + '\'' +
                ", transactionId='" + transactionId + '\'' +
                ", boundProfilePackage='" + boundProfilePackage + '\'' +
                '}';
    }
}
