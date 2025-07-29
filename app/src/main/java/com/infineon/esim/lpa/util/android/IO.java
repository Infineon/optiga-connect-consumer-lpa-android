/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.util.android;

import android.content.res.Resources;

import com.infineon.esim.util.Log;

import java.io.InputStream;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

public class IO {
    private static final String TAG = IO.class.getName();

    public static Certificate readCertificateFromResource(Resources resources, int resourceId) {

        InputStream inputStream = resources.openRawResource(resourceId);

        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            return certificateFactory.generateCertificate(inputStream);
        } catch (CertificateException e) {
            Log.error(TAG,"Error: CertificateException during reading of root CA certificates from resource.", e);
        }

        return null;
    }
}
