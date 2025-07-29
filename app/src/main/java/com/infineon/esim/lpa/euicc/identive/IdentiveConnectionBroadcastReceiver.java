/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.euicc.identive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;

import com.infineon.esim.lpa.Application;
import com.infineon.esim.util.Log;

import java.util.HashMap;
import java.util.Objects;

public class IdentiveConnectionBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = IdentiveConnectionBroadcastReceiver.class.getName();

    private final Context context;
    private final OnDisconnectCallback onDisconnectCallback;

    private static boolean hasBeenFreshlyAttached = false;
    private static String lastReaderName;

    public IdentiveConnectionBroadcastReceiver(Context context, OnDisconnectCallback onDisconnectCallback) {
        this.context = context;
        this.onDisconnectCallback = onDisconnectCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        Log.debug(TAG, "Received a broadcast.");
        Log.debug(TAG, "Action: " + action);

        if(action == null) {
            return;
        }

        switch (action) {
            case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                UsbDevice usbDevice;

                if(Build.VERSION.SDK_INT >=  Build.VERSION_CODES.TIRAMISU) {
                    usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice.class);
                } else {
                    usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                }

                if(usbDevice == null) {
                    Log.error(TAG,"USB device is null.");
                    return;
                }

                lastReaderName = usbDevice.getProductName();

                Log.info(TAG,"USB reader \"" + lastReaderName + "\" attached.");
                hasBeenFreshlyAttached = true;

                /* Do not directly initialize because of user prompt. Only in onResume method in the
                   activity (e.g. ProfileListActivity).
                 */
                break;
            case UsbManager.ACTION_USB_DEVICE_DETACHED:
                onDisconnectCallback.onDisconnect();
                break;
            default:
                Log.error(TAG, "Unknown action: " + intent.getAction());
        }
    }

    public void registerReceiver() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        context.registerReceiver(this, filter);
    }

    public static Boolean hasBeenFreshlyAttached() throws Exception {
        if(hasBeenFreshlyAttached) {
            hasBeenFreshlyAttached = false;
            if(isValidReaderName(lastReaderName)) {
                return true;
            } else {
                throw new Exception("Reader \"" + lastReaderName + "\" not supported.");
            }
        } else {
            return false;
        }
    }

    public static boolean isDeviceAttached() {
        UsbManager usbManager = Application.getUsbManager();

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            Log.debug(TAG, "USB device attached: " + device.getProductName());

            return isValidReaderName(device.getProductName());
        }

        return false;
    }

    public interface OnDisconnectCallback {
        void onDisconnect();
    }

    private static boolean isValidReaderName(String readerName) {
        for(String validReaderName : IdentiveEuiccInterface.READER_NAMES) {
            if (readerName.equals(validReaderName)) {
                return true;
            }
        }

        return false;
    }
}
