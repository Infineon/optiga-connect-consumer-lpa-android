/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos;

import com.gsma.sgp.messages.rspdefinitions.Octet32;
import com.gsma.sgp.messages.rspdefinitions.TransactionId;
import com.infineon.esim.util.Bytes;
import com.infineon.esim.util.Log;
import com.infineon.esim.util.crypto.HashUtils;


public class ConfirmationCode {
    private static final String TAG = ConfirmationCode.class.getName();

    public static Octet32 getHashCC(String confirmationCode, TransactionId transactionId) {
        if(confirmationCode != null) {
            return new Octet32(Bytes.decodeHexString(getCcHash(transactionId, confirmationCode)));
        }

        return null;
    }

    private static String getCcHash(TransactionId transactionId, String confirmationCode) {
        // Calculate the confirmation code hash as follows: H(H(confirmationCode) | transactionId)
        byte[] transactionIdBytes = transactionId.value;
        byte[] confirmationCodeBytes = confirmationCode.getBytes();

        byte[] hash1Bytes = HashUtils.hashSha256(confirmationCodeBytes);

        byte[] hash2Input = new byte[hash1Bytes.length + transactionIdBytes.length];

        System.arraycopy(hash1Bytes, 0, hash2Input, 0, hash1Bytes.length);
        System.arraycopy(transactionIdBytes, 0, hash2Input, hash1Bytes.length, transactionIdBytes.length);


        byte[] hash2Bytes = HashUtils.hashSha256(hash2Input);

        Log.debug(TAG, " - Transaction ID: " + Bytes.encodeHexString(transactionIdBytes));
        Log.debug(TAG, " - Confirmation Code: " + Bytes.encodeHexString(confirmationCodeBytes));
        Log.debug(TAG, " - Hash CC input: " + Bytes.encodeHexString(hash2Input));
        Log.debug(TAG, " - Hash CC result: " + Bytes.encodeHexString(hash2Bytes));

        return Bytes.encodeHexString(hash2Bytes);
    }
}
