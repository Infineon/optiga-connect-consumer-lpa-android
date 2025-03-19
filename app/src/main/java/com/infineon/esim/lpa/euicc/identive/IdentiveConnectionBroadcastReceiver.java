/*
 * THE SOURCE CODE AND ITS RELATED DOCUMENTATION IS PROVIDED "AS IS". INFINEON
 * TECHNOLOGIES MAKES NO OTHER WARRANTY OF ANY KIND,WHETHER EXPRESS,IMPLIED OR,
 * STATUTORY AND DISCLAIMS ANY AND ALL IMPLIED WARRANTIES OF MERCHANTABILITY,
 * SATISFACTORY QUALITY, NON INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE.
 *
 * THE SOURCE CODE AND DOCUMENTATION MAY INCLUDE ERRORS. INFINEON TECHNOLOGIES
 * RESERVES THE RIGHT TO INCORPORATE MODIFICATIONS TO THE SOURCE CODE IN LATER
 * REVISIONS OF IT, AND TO MAKE IMPROVEMENTS OR CHANGES IN THE DOCUMENTATION OR
 * THE PRODUCTS OR TECHNOLOGIES DESCRIBED THEREIN AT ANY TIME.
 *
 * INFINEON TECHNOLOGIES SHALL NOT BE LIABLE FOR ANY DIRECT, INDIRECT OR
 * CONSEQUENTIAL DAMAGE OR LIABILITY ARISING FROM YOUR USE OF THE SOURCE CODE OR
 * ANY DOCUMENTATION, INCLUDING BUT NOT LIMITED TO, LOST REVENUES, DATA OR
 * PROFITS, DAMAGES OF ANY SPECIAL, INCIDENTAL OR CONSEQUENTIAL NATURE, PUNITIVE
 * DAMAGES, LOSS OF PROPERTY OR LOSS OF PROFITS ARISING OUT OF OR IN CONNECTION
 * WITH THIS AGREEMENT, OR BEING UNUSABLE, EVEN IF ADVISED OF THE POSSIBILITY OR
 * PROBABILITY OF SUCH DAMAGES AND WHETHER A CLAIM FOR SUCH DAMAGE IS BASED UPON
 * WARRANTY, CONTRACT, TORT, NEGLIGENCE OR OTHERWISE.
 *
 * (C)Copyright INFINEON TECHNOLOGIES All rights reserved
 */

package com.infineon.esim.lpa.euicc.identive;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;

import com.infineon.esim.lpa.Application;
import com.infineon.esim.util.Log;

import java.util.HashMap;

public class IdentiveConnectionBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = IdentiveConnectionBroadcastReceiver.class.getName();

    private final Context context;
    private final OnDisconnectCallback onDisconnectCallback;

    private static boolean hasBeenFreshlyAttached = false;

    public IdentiveConnectionBroadcastReceiver(Context context, OnDisconnectCallback onDisconnectCallback) {
        this.context = context;
        this.onDisconnectCallback = onDisconnectCallback;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.debug(TAG, "Received a broadcast.");
        Log.debug(TAG, "Action: " + intent.getAction());

        switch (intent.getAction()) {
            case UsbManager.ACTION_USB_DEVICE_ATTACHED:
                hasBeenFreshlyAttached = true;
//                /* Do not directly initialize because of user prompt. Only in onResume method in the
//                   activity (e.g. ProfileListActivity).
//                 */
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

    public static Boolean hasBeenFreshlyAttached() {
        if(hasBeenFreshlyAttached) {
            hasBeenFreshlyAttached = false;
            return true;
        } else {
            return false;
        }
    }

    public static boolean isDeviceAttached() {
        UsbManager usbManager = Application.getUsbManager();

        HashMap<String, UsbDevice> deviceList = usbManager.getDeviceList();
        for (UsbDevice device : deviceList.values()) {
            Log.debug(TAG, "USB device attached: " + device.getProductName());

            if (device.getProductName().equals(IdentiveEuiccInterface.READER_NAME)) {
                return true;
            }
        }

        return false;
    }

    public interface OnDisconnectCallback {
        void onDisconnect();
    }
}
