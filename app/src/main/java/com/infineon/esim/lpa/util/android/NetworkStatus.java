/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.util.android;

import android.net.ConnectivityManager;
import android.net.Network;

import androidx.annotation.NonNull;

import com.infineon.esim.lpa.Application;
import com.infineon.esim.util.Log;

public class NetworkStatus {
    private static final String TAG = NetworkStatus.class.getName();

    private static boolean isNetworkAvailable;

    public static void registerNetworkCallback() {
        ConnectivityManager connectivityManager = Application.getConnectivityManager();
        connectivityManager.registerDefaultNetworkCallback(new ConnectivityManager.NetworkCallback(){
            @Override
            public void onAvailable(@NonNull Network network) {
                Log.debug(TAG, "Network now available.");
                isNetworkAvailable = true;
                super.onAvailable(network);
            }

            @Override
            public void onLosing(@NonNull Network network, int maxMsToLive) {
                Log.debug(TAG, "Network now losing.");
                isNetworkAvailable = false;
                super.onLosing(network, maxMsToLive);
            }

            @Override
            public void onLost(@NonNull Network network) {
                Log.debug(TAG, "Network now lost.");
                isNetworkAvailable = false;
                super.onLost(network);
            }

            @Override
            public void onUnavailable() {
                Log.debug(TAG, "Network now unavailable.");
                isNetworkAvailable = false;
                super.onUnavailable();
            }
        });
    }

    public static boolean isNetworkAvailable() {
        return isNetworkAvailable;
    }
}
