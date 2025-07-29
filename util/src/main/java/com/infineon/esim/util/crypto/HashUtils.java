/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.util.crypto;

import com.infineon.esim.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@SuppressWarnings("unused")
public class HashUtils {
    private static final String TAG = HashUtils.class.getName();

    private static final String DIGEST_ALGO_SHA256 = "SHA-256";

    public static byte[] hashSha256(byte[] input) {
        MessageDigest messageDigest = getMessageDigest(DIGEST_ALGO_SHA256);

        messageDigest.update(input, 0, input.length);

        return messageDigest.digest();
    }

    public static byte[] hashSha256(String input) {
        byte[] inputBytes = input.getBytes();

        return hashSha256(inputBytes);
    }

    @SuppressWarnings("SameParameterValue")
    private static MessageDigest getMessageDigest(String algorithm) {
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            Log.error(TAG, "Error: NoSuchAlgorithmException during SCP11a message digest calculation for algorithm \"" + algorithm + "\".");
        }

        return digest;
    }
}
