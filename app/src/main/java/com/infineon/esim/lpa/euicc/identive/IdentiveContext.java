/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.identive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;

import com.infineon.esim.util.Log;

final public class IdentiveContext extends ContextWrapper {
    private static final String TAG = IdentiveContext.class.getName();

    private BroadcastReceiver broadcastReceiver = null;

    IdentiveContext(Context context) {
        super(context);
    }

    @Override
    protected void finalize() throws Throwable {
        Log.debug(TAG, "Finalizing IdentiveContext.");
        try {
            unregisterReceiver();
        } finally {
            super.finalize();
        }
    }
    
    @Override
    public Intent registerReceiver(BroadcastReceiver receiver, IntentFilter filter) {
        Log.debug(TAG, "Registering receiver");
        broadcastReceiver = receiver;
        return super.registerReceiver(receiver, filter);
    }

    // fixed resource leak in SCard.class
    // - missing a call to unregisterReceiver()
    public void unregisterReceiver() {
        if (broadcastReceiver != null) {
            Log.debug(TAG, "Unregistering receiver.");
            super.unregisterReceiver(broadcastReceiver);
            broadcastReceiver = null;
        }
    }
}
