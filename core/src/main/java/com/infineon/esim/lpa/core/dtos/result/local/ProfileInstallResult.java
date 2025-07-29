/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.local;

import com.gsma.sgp.messages.rspdefinitions.ProfileInstallationResult;
import com.gsma.sgp.messages.rspdefinitions.ProfileInstallationResultData;

import java.util.HashMap;

public class ProfileInstallResult implements OperationResult {
    private boolean isSuccess = false;

    private String aid = null;
    private String ppiResponse = null;
    private int errorReason = 0;
    private int bppCommandId = 0;

    public ProfileInstallResult(ProfileInstallationResult profileInstallationResult) {
        if (profileInstallationResult.getProfileInstallationResultData() == null) {
            return;
        }
        ProfileInstallationResultData resultData = profileInstallationResult.getProfileInstallationResultData();
        if (resultData.getFinalResult() == null) {
            return;
        }
        ProfileInstallationResultData.FinalResult finalResult = resultData.getFinalResult();
        if (finalResult.getSuccessResult() != null) {
            isSuccess = true;
            aid = finalResult.getSuccessResult().getAid().toString();
            ppiResponse = finalResult.getSuccessResult().getPpiResponse().toString();
            return;
        }
        if (finalResult.getErrorResult() != null) {
            errorReason = finalResult.getErrorResult().getErrorReason().intValue();
            bppCommandId = finalResult.getErrorResult().getBppCommandId().intValue();
            ppiResponse = finalResult.getErrorResult().getPpiResponse().toString();
        }
    }

    @Override
    public boolean isOk() {
        return isSuccess;
    }

    // TODO
    @Override
    public boolean equals(int value) {
        return errorReason == value;
    }

    @Override
    public String getDescription() {
        if (isSuccess) {
            return "ES10 ProfileInstallationResult\n" +
                    "Status: Success\n" +
                    "AID: " + aid + "\n" +
                    "PPI Response: " + ppiResponse;
        } else {
            return "ES10 ProfileInstallationResult\n" +
                    "Status: Error\n" +
                    "BPP Command ID: " + BppCommandId.getDescription(bppCommandId) + "\n" +
                    "Error Reason: " + ErrorReason.getDescription(errorReason) + "\n" +
                    "PPI Response: " + ppiResponse;
        }
    }

    static class BppCommandId {
        public static final int INITIALISE_SECURE_CHANNEL = 0;
        public static final int CONFIGURE_ISDP = 1;
        public static final int STORE_METADATA = 2;
        public static final int STORE_METADATA_2 = 3;
        public static final int REPLACE_SESSION_KEYS = 4;
        public static final int LOAD_PROFILE_ELEMENTS = 5;

        private static final HashMap<Integer, String> lookup;

        static {
            lookup = new HashMap<>();
            lookup.put(INITIALISE_SECURE_CHANNEL,"initialiseSecureChannel");
            lookup.put(CONFIGURE_ISDP, "configureISDP");
            lookup.put(STORE_METADATA, "storeMetadata");
            lookup.put(STORE_METADATA_2, "storeMetadata2");
            lookup.put(REPLACE_SESSION_KEYS, "replaceSessionKeys");
            lookup.put(LOAD_PROFILE_ELEMENTS, "loadProfileElements");
        }

        public static String getDescription(int bppCommandId) {
            return lookup.get(bppCommandId);
        }
    }

    static class ErrorReason {
        public static final int INCORRECT_INPUT_VALUES = 1;
        public static final int INVALID_SIGNATURE = 2;
        public static final int INVALID_TRANSACTION_ID = 3;
        public static final int UNSUPPORTED_CRT_VALUES = 4;
        public static final int UNSUPPORTED_REMOTE_OPERATION_TYPE = 5;
        public static final int UNSUPPORTED_PROFILE_CLASS = 6;
        public static final int BSP_STRUCTURE_ERROR = 7;
        public static final int BSP_SECURITY_ERROR = 8;
        public static final int INSTALL_FAILED_DUE_TO_ICCID_ALREADY_EXISTS_ON_EUICC = 9;
        public static final int INSTALL_FAILED_DUE_TO_INSUFFICIENT_MEMORY_FOR_PROFILE = 10;
        public static final int INSTALL_FAILED_DUE_TO_INTERRUPTION = 11;
        public static final int INSTALL_FAILED_DUE_TO_PE_PROCESSING_ERROR = 12;
        public static final int INSTALL_FAILED_DUE_TO_DATA_MISMATCH = 13;
        public static final int TEST_PROFILE_INSTALL_FAILED_DUE_TO_INVALID_NAA_KEY = 14;
        public static final int PPR_NOT_ALLOWED = 15;
        public static final int ENTERPRISE_PROFILES_NOT_SUPPORTED = 17;
        public static final int ENTERPRISE_RULES_NOT_ALLOWED = 18;
        public static final int ENTERPRISE_PROFILE_NOT_ALLOWED = 19;
        public static final int ENTERPRISE_OID_MISMATCH = 20;
        public static final int ENTERPRISE_RULES_ERROR = 21;
        public static final int ENTERPRISE_PROFILES_ONLY = 22;
        public static final int LPR_NOT_SUPPORTED = 23;
        public static final int UNKNOWN_TLV_IN_METADATA = 26;
        public static final int INSTALL_FAILED_DUE_TO_UNKNOWN_ERROR = 127;

        private static final HashMap<Integer, String> lookup;

        static {
            lookup = new HashMap<>();
            lookup.put(INCORRECT_INPUT_VALUES,"incorrectInputValues");
            lookup.put(INVALID_SIGNATURE, "invalidSignature");
            lookup.put(INVALID_TRANSACTION_ID, "invalidTransactionId");
            lookup.put(UNSUPPORTED_CRT_VALUES, "unsupportedCrtValues");
            lookup.put(UNSUPPORTED_REMOTE_OPERATION_TYPE, "unsupportedRemoteOperationType");
            lookup.put(UNSUPPORTED_PROFILE_CLASS, "unsupportedProfileClass");
            lookup.put(BSP_STRUCTURE_ERROR, "bspStructureError");
            lookup.put(BSP_SECURITY_ERROR, "bspSecurityError");
            lookup.put(INSTALL_FAILED_DUE_TO_ICCID_ALREADY_EXISTS_ON_EUICC, "installFailedDueToIccidAlreadyExistsOnEuicc");
            lookup.put(INSTALL_FAILED_DUE_TO_INSUFFICIENT_MEMORY_FOR_PROFILE, "installFailedDueToInsufficientMemoryForProfile");
            lookup.put(INSTALL_FAILED_DUE_TO_INTERRUPTION, "installFailedDueToInterruption");
            lookup.put(INSTALL_FAILED_DUE_TO_PE_PROCESSING_ERROR, "installFailedDueToPEProcessingError");
            lookup.put(INSTALL_FAILED_DUE_TO_DATA_MISMATCH, "installFailedDueToDataMismatch");
            lookup.put(TEST_PROFILE_INSTALL_FAILED_DUE_TO_INVALID_NAA_KEY, "testProfileInstallFailedDueToInvalidNaaKey");
            lookup.put(PPR_NOT_ALLOWED, "pprNotAllowed");
            lookup.put(ENTERPRISE_PROFILES_NOT_SUPPORTED, "enterpriseProfilesNotSupported");
            lookup.put(ENTERPRISE_RULES_NOT_ALLOWED, "enterpriseRulesNotAllowed");
            lookup.put(ENTERPRISE_PROFILE_NOT_ALLOWED, "enterpriseProfileNotAllowed");
            lookup.put(ENTERPRISE_OID_MISMATCH, "enterpriseOidMismatch");
            lookup.put(ENTERPRISE_RULES_ERROR, "enterpriseRulesError");
            lookup.put(ENTERPRISE_PROFILES_ONLY, "enterpriseProfilesOnly");
            lookup.put(LPR_NOT_SUPPORTED, "lprNotSupported");
            lookup.put(UNKNOWN_TLV_IN_METADATA, "unknownTlvInMetadata");
            lookup.put(INSTALL_FAILED_DUE_TO_UNKNOWN_ERROR, "installFailedDueToUnknownError");
        }

        public static String getDescription(int errorReason) {
            return lookup.get(errorReason);
        }
    }
}
