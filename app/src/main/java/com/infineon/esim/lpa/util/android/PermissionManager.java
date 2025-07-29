/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.util.android;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.infineon.esim.lpa.data.Preferences;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.List;

public class PermissionManager {
    private static final String TAG = PermissionManager.class.getName();


    public interface Callback {
        void onComplete(Boolean allGranted);
    }

    private Context context;
    private Callback callback;

    private final List<PermissionRequest> permissionRequests;

    private final ActivityResultLauncher<String> permissionCheck;

    public static class PermissionRequest {
        private final String permissionName;
        private final String rationaleTitle;
        private final String rationaleBody;

        private int requestCounter = 2;

        public PermissionRequest(String permissionName, String rationaleTitle, String rationaleBody) {
            this.permissionName = permissionName;
            this.rationaleTitle = rationaleTitle;
            this.rationaleBody = rationaleBody;
        }

        public String getPermissionName() {
            return permissionName;
        }

        public String getRationaleTitle() {
            return rationaleTitle;
        }

        public String getRationaleBody() {
            return rationaleBody;
        }

        public void decrementCounter() {
            this.requestCounter--;
        }

        public boolean shouldAskForPermission() {
            return this.requestCounter > 0;
        }
    }

    public PermissionManager(@NonNull Fragment fragment) {
        this.permissionRequests = new ArrayList<>();
        permissionCheck = fragment.registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    Log.debug(TAG, "Permission is granted: " + isGranted);

                    // Handle the rest of the permissions
                    handleNotGrantedPermissionRequests();
                });
    }

    public PermissionManager(@NonNull FragmentActivity fragmentActivity) {
        this.permissionRequests = new ArrayList<>();
        permissionCheck = fragmentActivity.registerForActivityResult(new ActivityResultContracts.RequestPermission(),
                isGranted -> {
                    Log.debug(TAG, "Permission is granted: " + isGranted);

                    // Handle the rest of the permissions
                    handleNotGrantedPermissionRequests();
                });
    }

    public PermissionManager context(Context context) {
        this.context = context;
        return this;
    }

    public PermissionManager request(List<PermissionRequest> permissionRequests) {
        this.permissionRequests.addAll(permissionRequests);
        return this;
    }


    public void checkPermission(Callback callback) {
        Log.debug(TAG, "Checking permissions.");
        this.callback = callback;

        handleNotGrantedPermissionRequests();
    }

    private void handleNotGrantedPermissionRequests() {
        for(PermissionRequest permissionRequest : permissionRequests) {
            if(!hasPermission(permissionRequest.getPermissionName())) {
                if (permissionRequest.shouldAskForPermission() && !Preferences.getPermissionFinallyDenied(permissionRequest.getPermissionName())) {
                    Log.debug(TAG, "Shall ask for permission for \"" + permissionRequest.getPermissionName() + "\"");
                    displayRationale(permissionRequest);
                    return;
                } else {
                    Log.debug(TAG, "Finally denied permission for \"" + permissionRequest.getPermissionName() + "\"");
                    Preferences.setPermissionFinallyDenied(permissionRequest.getPermissionName());
                    sendNegativeResult();
                    return;
                }
            }
        }

        sendPositiveResult();
    }

    private void displayRationale(PermissionRequest permissionRequest) {
        new AlertDialog.Builder(context)
                .setTitle(permissionRequest.getRationaleTitle())
                .setMessage(permissionRequest.getRationaleBody())
                .setCancelable(false)
                .setPositiveButton("OK", (dialog, which) -> requestPermission(permissionRequest)).show();
    }

    public void requestPermission(PermissionRequest permissionRequest) {
        Log.debug(TAG, "Requesting permission \"" + permissionRequest.getPermissionName() + "\"");
        Log.debug(TAG, "Tries left: " + permissionRequest.requestCounter);
        permissionRequest.decrementCounter();

        permissionCheck.launch(permissionRequest.getPermissionName());
    }

    private void sendPositiveResult() {
        callback.onComplete(true);
        cleanUp();
    }

    private void sendNegativeResult() {
        callback.onComplete(false);
        cleanUp();
    }

    private void cleanUp() {
        permissionRequests.clear();
        callback = null;
    }

    private boolean hasPermission(String permission) {
        Log.debug(TAG, "Checking permission \"" + permission + "\"");
        boolean hasPermission = ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
        Log.debug(TAG, "Has permission \"" + permission + "\": " + hasPermission);
        return hasPermission;
    }
}
