/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.local;

import com.gsma.sgp.messages.rspdefinitions.AuthenticateServerResponse;

import java.util.HashMap;

public class AuthenticateServerResp implements OperationResult {
    private boolean isSuccess = false;

    private int authenticateErrorCode = 0;

    public AuthenticateServerResp(AuthenticateServerResponse authenticateServerResponse) {
        if(authenticateServerResponse == null) {
            return;
        }

        if (authenticateServerResponse.getAuthenticateResponseOk() != null) {
            isSuccess = true;
            return;
        }
        if (authenticateServerResponse.getAuthenticateResponseError() != null) {
            authenticateErrorCode =
                    authenticateServerResponse.getAuthenticateResponseError().getAuthenticateErrorCode().intValue();
        }
    }

    @Override
    public boolean isOk() {
        return isSuccess;
    }

    @Override
    public boolean equals(int value) {
        return authenticateErrorCode == value;
    }

    @Override
    public String getDescription() {
        if (isSuccess) {
            return "ES10 AuthenticateServerResponse\n" +
                    "Status: Success";
        } else {
            return "ES10 AuthenticateServerResponse\n" +
                    "Status: Error\n" +
                    "Authenticate Error Code: " + AuthenticateErrorCode.getDescription(authenticateErrorCode);
        }
    }

    static class AuthenticateErrorCode {
        public static final int INVALID_CERTIFICATE = 1;
        public static final int INVALID_SIGNATURE = 2;
        public static final int UNSUPPORTED_CURVE = 3;
        public static final int NO_SESSION = 4;
        public static final int INVALID_OID = 5;
        public static final int EUICC_CHALLENGE_MISMATCH = 6;
        public static final int CI_PK_UNKNOWN = 7;
        public static final int TRANSACTION_ID_ERROR = 8;
        public static final int MISSING_CRL = 9;
        public static final int INVALID_CRL_SIGNATURE = 10;
        public static final int REVOKED_CERT = 11;
        public static final int INVALID_CERT_OR_CRL_TIME = 12;
        public static final int INVALID_CERT_OR_CRL_CONFIGURATION = 13;
        public static final int INVALID_ICCID = 14;
        public static final int UNDEFINED_ERROR = 127;

        private static final HashMap<Integer, String> lookup;

        static {
            lookup = new HashMap<>();
            lookup.put(INVALID_CERTIFICATE,"invalidCertificate");
            lookup.put(INVALID_SIGNATURE, "invalidSignature");
            lookup.put(UNSUPPORTED_CURVE, "unsupportedCurve");
            lookup.put(NO_SESSION, "noSession");
            lookup.put(INVALID_OID, "invalidOid");
            lookup.put(EUICC_CHALLENGE_MISMATCH, "euiccChallengeMismatch");
            lookup.put(CI_PK_UNKNOWN, "ciPKUnknown");
            lookup.put(TRANSACTION_ID_ERROR, "transactionIdError");
            lookup.put(MISSING_CRL, "missingCrl");
            lookup.put(INVALID_CRL_SIGNATURE, "invalidCrlSignature");
            lookup.put(REVOKED_CERT, "revokedCert");
            lookup.put(INVALID_CERT_OR_CRL_TIME, "invalidCertOrCrlTime");
            lookup.put(INVALID_CERT_OR_CRL_CONFIGURATION, "invalidCertOrCrlConfiguration");
            lookup.put(INVALID_ICCID, "invalidIccid");
            lookup.put(UNDEFINED_ERROR, "undefinedError");
        }

        public static String getDescription(int value) {
            return lookup.get(value);
        }
    }
}
