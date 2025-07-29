/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.hardware.usb.UsbManager;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;

import androidx.preference.PreferenceManager;

import com.infineon.esim.lpa.core.es9plus.TlsUtil;
import com.infineon.esim.lpa.data.DataModel;
import com.infineon.esim.lpa.data.Preferences;
import com.infineon.esim.lpa.util.android.IO;
import com.infineon.esim.lpa.util.android.NetworkStatus;
import com.infineon.esim.util.Log;

import java.security.cert.Certificate;
import java.util.ArrayList;
import java.util.List;

public class Application extends android.app.Application {
    private static final String TAG = Application.class.getName();

    private static Context applicationContext;
    private static Resources resources;

    // Intent extra identifiers
    public static final String INTENT_EXTRA_PROFILE_METADATA = "com.infineon.esim.lpa.PROFILE_METADATA";
    public static final String INTENT_EXTRA_ACTIVATION_CODE = "com.infineon.esim.lpa.ACTIVATION_CODE";

    @Override
    public void onCreate() {
        Log.debug(TAG, "Initializing application.");
        super.onCreate();

        // Initialize resources and app context
        resources = getResources();
        applicationContext = getApplicationContext();

        // Register network callback for network status
        NetworkStatus.registerNetworkCallback();

        // Initialize data model and preferences holder
        DataModel.initializeInstance(this);
        Preferences.initializeInstance();

        // Set trusted Root CAs
        initializeTrustedRootCas();
    }

    public static Context getAppContext() {
        return applicationContext;
    }

    public static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(applicationContext);
    }

    public static String getStringResource(int id) {
        return resources.getString(id);
    }

    public static PackageManager getPacketManager() {
        return applicationContext.getPackageManager();
    }

    public static UsbManager getUsbManager() {
        return (UsbManager) applicationContext.getSystemService(Context.USB_SERVICE);
    }

    public static ConnectivityManager getConnectivityManager() {
        return (ConnectivityManager) applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public static WifiManager getWifiManager() {
        return (WifiManager) applicationContext.getSystemService(Context.WIFI_SERVICE);
    }

    public static void initializeTrustedRootCas() {
        List<Certificate> liveCertificates = new ArrayList<>();
        liveCertificates.add(IO.readCertificateFromResource(resources, R.raw.symantec_gsma_rspv2_root_ci1_pem));

        List<Certificate> testCertificates = new ArrayList<>();
        testCertificates.add(IO.readCertificateFromResource(resources, R.raw.gsma_test_root_ca_cert_pem));
        testCertificates.add(IO.readCertificateFromResource(resources, R.raw.gsma_root_ci_test_1_2_pem));
        testCertificates.add(IO.readCertificateFromResource(resources, R.raw.gsma_root_ci_test_1_5_pem));
        testCertificates.add(IO.readCertificateFromResource(resources, R.raw.thales_pem));
        TlsUtil.initializeCertificates(liveCertificates, testCertificates);

        boolean trustTestCertificates = Preferences.getTrustGsmaTestCi();
        TlsUtil.setTrustLevel(trustTestCertificates);
    }
}
