/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.local;

import com.gsma.sgp.messages.rspdefinitions.PrepareDownloadResponse;

import java.util.HashMap;

public class PrepareDownloadResp implements OperationResult {
    private boolean isSuccess = false;

    private int downloadErrorCode = 0;

    public PrepareDownloadResp(PrepareDownloadResponse prepareDownloadResponse) {
        if(prepareDownloadResponse == null) {
            return;
        }

        if (prepareDownloadResponse.getDownloadResponseOk() != null) {
            isSuccess = true;
            return;
        }
        if (prepareDownloadResponse.getDownloadResponseError() != null) {
            downloadErrorCode =
                    prepareDownloadResponse.getDownloadResponseError().getDownloadErrorCode().intValue();
        }
    }

    @Override
    public boolean isOk() {
        return isSuccess;
    }

    @Override
    public boolean equals(int value) {
        return downloadErrorCode == value;
    }

    @Override
    public String getDescription() {
        if (isSuccess) {
            return "ES10 PrepareDownloadResponse\n" +
                    "Status: Success";
        } else {
            return "ES10 PrepareDownloadResponse\n" +
                    "Status: Error\n" +
                    "Download Error Code: " + DownloadErrorCode.getDescription(downloadErrorCode);
        }
    }

    static class DownloadErrorCode {
        public static final int INVALID_CERTIFICATE = 1;
        public static final int INVALID_SIGNATURE = 2;
        public static final int UNSUPPORTED_CURVE = 3;
        public static final int NO_SESSION = 4;
        public static final int INVALID_TRANSACTION_ID = 5;
        public static final int UNDEFINED_ERROR = 127;

        private static final HashMap<Integer, String> lookup;

        static {
            lookup = new HashMap<>();
            lookup.put(INVALID_CERTIFICATE,"invalidCertificate");
            lookup.put(INVALID_SIGNATURE, "invalidSignature");
            lookup.put(UNSUPPORTED_CURVE, "unsupportedCurve");
            lookup.put(NO_SESSION, "noSession");
            lookup.put(INVALID_TRANSACTION_ID, "invalidTransactionId");
            lookup.put(UNDEFINED_ERROR, "undefinedError");
        }

        public static String getDescription(int value) {
            return lookup.get(value);
        }
    }
}
