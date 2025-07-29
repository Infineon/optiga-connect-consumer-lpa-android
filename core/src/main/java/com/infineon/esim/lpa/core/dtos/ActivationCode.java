/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class ActivationCode implements Parcelable {
    private String activationCode;

    private final Boolean isValid;
    private String acFormat;
    private String smdpServer;
    private String matchingId;
    private String smdpOid;
    private String confirmationCodeRequiredFlag;

    public ActivationCode(String barcode) {
        this.isValid = parseActivationCode(barcode);
    }

    public ActivationCode(String acFormat, String smdpServer, String matchingID) {
        boolean validity;

        this.acFormat = acFormat;
        this.smdpServer = smdpServer;
        this.matchingId = matchingID;

        this.activationCode = String.join("$", acFormat, smdpServer, matchingID);

        validity = (acFormat.compareTo("1") == 0);

        validity &= !acFormat.isEmpty() && !smdpServer.isEmpty() && !matchingId.isEmpty();

        this.isValid = validity;
    }

    protected ActivationCode(Parcel in) {
        activationCode = in.readString();
        this.isValid = parseActivationCode(activationCode);
    }

    public Boolean isValid() {
        return isValid;
    }

    public String getAcFormat() {
        return acFormat;
    }

    public String getSmdpServer() {
        return smdpServer;
    }

    public String getMatchingId() {
        return matchingId;
    }

    public String getSmdpOid() {
        return smdpOid;
    }

    public String getConfirmationCodeRequiredFlag() {
        return confirmationCodeRequiredFlag;
    }

    // Example
    // Input activation code:
    //       LPA:1$trl.prod.ondemandconnectivity.com$KJ912512WD7N5NMZ
    //
    // Output
    //       prefix:        LPA:1
    //       matchingId:    KJ912512WD7N5NMZ
    //       smdpServer:    trl.prod.ondemandconnectivity.com
    //       rspServerUrl:  https://trl.prod.ondemandconnectivity.com
    private Boolean parseActivationCode(String barcode) {
        boolean validity = false;

        // Remove trailing "LPA:" if present
        this.activationCode = barcode.replace("LPA:", "");

        String[] parts = activationCode.split("\\$");
        if((parts.length >= 3) && (parts.length <= 5)) {
            acFormat = parts[0];
            smdpServer = parts[1];
            matchingId = parts[2];

            // AC_Format must be "1"
            validity = (acFormat.compareTo("1") == 0);

            // All first three parts must be non-empty
            validity &= !acFormat.isEmpty() && !smdpServer.isEmpty() && !matchingId.isEmpty();

            if(parts.length >= 4) {
                smdpOid = parts[3];
            }

            // If AC has 4 $s the SMDP OID must be non-empty
            if(parts.length == 4) {
                validity &= !smdpOid.isEmpty();
            }

            // If AC has 5 $s the CC required flag must be non-empty
            if(parts.length == 5) {
                confirmationCodeRequiredFlag = parts[4];
                validity &= !confirmationCodeRequiredFlag.isEmpty();
            }

            // Last character shall not be a $
            String lastCharacter = activationCode.substring(activationCode.length() - 1);
            validity &= (lastCharacter.compareTo("$") != 0);
        }

        return validity;
    }

    @Override
    @NonNull
    public String toString() {
        if(activationCode != null) {
            return activationCode;
        } else {
            return "N/A";
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ActivationCode> CREATOR = new Creator<ActivationCode>() {
        @Override
        public ActivationCode createFromParcel(Parcel in) {
            return new ActivationCode(in);
        }

        @Override
        public ActivationCode[] newArray(int size) {
            return new ActivationCode[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(activationCode);
    }
}
