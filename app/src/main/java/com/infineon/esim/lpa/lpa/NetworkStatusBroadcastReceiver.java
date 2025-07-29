/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.lpa;

import android.net.ConnectivityManager;
import android.net.Network;

import androidx.annotation.NonNull;

import com.infineon.esim.lpa.Application;
import com.infineon.esim.lpa.util.android.InternetConnectionConsumer;
import com.infineon.esim.lpa.util.android.NetworkStatus;
import com.infineon.esim.util.Log;

public class NetworkStatusBroadcastReceiver {
    private static final String TAG = NetworkStatusBroadcastReceiver.class.getName();

    private final InternetConnectionConsumer internetConnectionConsumer;

    public NetworkStatusBroadcastReceiver(InternetConnectionConsumer internetConnectionConsumer) {
        this.internetConnectionConsumer = internetConnectionConsumer;

        // Initiate internet connection consumer
        if(NetworkStatus.isNetworkAvailable()) {
            internetConnectionConsumer.onConnected();
        } else {
            internetConnectionConsumer.onDisconnected();
        }
    }

    public void registerReceiver() {
        ConnectivityManager connectivityManager = Application.getConnectivityManager();
        connectivityManager.registerDefaultNetworkCallback(networkCallback);
    }

    public void unregisterReceiver() {
        ConnectivityManager connectivityManager = Application.getConnectivityManager();
        connectivityManager.unregisterNetworkCallback(networkCallback);
    }

    private final ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
            Log.debug(TAG, "Network available!");
            super.onAvailable(network);
            internetConnectionConsumer.onConnected();
        }

        @Override
        public void onLost(@NonNull Network network) {
            Log.debug(TAG, "Network lost!");
            super.onLost(network);
            internetConnectionConsumer.onDisconnected();
        }

        @Override
        public void onUnavailable() {
            Log.debug(TAG, "Network unavailable!");
            super.onUnavailable();
            internetConnectionConsumer.onDisconnected();
        }
    };
}
