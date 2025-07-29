/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */
package com.infineon.esim.lpa.ui.scanBarcode;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.Preview;
import androidx.camera.core.resolutionselector.ResolutionSelector;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.BarcodeScanner;
import com.google.mlkit.vision.barcode.BarcodeScannerOptions;
import com.google.mlkit.vision.barcode.BarcodeScanning;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;
import com.infineon.esim.lpa.Application;
import com.infineon.esim.lpa.R;
import com.infineon.esim.lpa.core.dtos.ActivationCode;
import com.infineon.esim.lpa.ui.downloadProfile.DownloadActivity;
import com.infineon.esim.lpa.util.android.DialogHelper;
import com.infineon.esim.lpa.util.android.PermissionManager;
import com.infineon.esim.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

final public class ScanBarcodeActivity extends AppCompatActivity {
    private final static String TAG = ScanBarcodeActivity.class.getName();

    private final PermissionManager permissionManager = new PermissionManager(this);

    private TextView textViewCameraAvailable;
    private PreviewView cameraPreviewView;
    private TextView textViewBarCodeValue;
    private ActivationCode activationCode;
    private Button buttonUseThisCode;

    private ScanBarcodeViewModel viewModel;

    private ListenableFuture<ProcessCameraProvider> cameraProviderFuture;
    private ProcessCameraProvider cameraProvider;

    private boolean permissionRequestOngoing = false;

    // region Lifecycle management

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_barcode);

        this.viewModel = new ViewModelProvider(this).get(ScanBarcodeViewModel.class);

        // Attach the UI
        attachUi();
    }

    @Override
    protected void onResume() {
        Log.debug(TAG,"Resuming activity.");
        super.onResume();

        int numCameras = 0;
        try {
            numCameras = ((CameraManager) getSystemService(Context.CAMERA_SERVICE)).getCameraIdList().length;
        } catch (CameraAccessException e) {
            Log.error(TAG, "Cannot find camera manager", e);
        }

        if(!permissionRequestOngoing) {
            Log.debug(TAG,"No permission request ongoing. Initialize camera.");
            if(numCameras > 0){
                textViewCameraAvailable.setVisibility(View.GONE);
                initializeCamera();
            }
            else{
                textViewCameraAvailable.setVisibility(View.VISIBLE);
            }
        } else {
            Log.debug(TAG,"Permission request ongoing.");
        }
    }

    @Override
    protected void onPause() {
        Log.debug(TAG,"Pausing activity.");
        super.onPause();
        finishBarcodeScanner();
    }

    @Override
    public void onStop() {
        // App crashes on Oppo Reno Z when starting with Android studio if not used...
        super.onStop();
        finish();
    }

    // endregion

    // region Camera handling

    private void initializeCamera() {
        List<PermissionManager.PermissionRequest> permissionRequests = new ArrayList<>();

        // Check camera permission
        permissionRequests.add(new PermissionManager.PermissionRequest(
                Manifest.permission.CAMERA,
                getString(R.string.scan_qr_camera_permission_alert_dialog_title),
                getString(R.string.scan_qr_camera_permission_alert_dialog_message)));

        permissionRequestOngoing = true;

        permissionManager.request(permissionRequests)
                .context(this)
                .checkPermission(allGranted -> {

                    if (allGranted) {
                        permissionRequestOngoing = false;
                        Log.debug(TAG, "Needed CAMERA permission is granted.");
                        cameraProviderFuture = ProcessCameraProvider.getInstance(this);

                        cameraProviderFuture.addListener(() -> {
                            try {
                                cameraProvider = cameraProviderFuture.get();
                                CameraSelector cameraSelector = getCamera();
                                Preview preview = initializePreview();
                                ImageAnalysis imageAnalysis = initializeBarcodeScanner();

                                cameraProvider.unbindAll();
                                cameraProvider.bindToLifecycle(this, cameraSelector, imageAnalysis, preview);

                            } catch (ExecutionException | InterruptedException e) {
                                // No errors need to be handled for this Future.
                                // This should never be reached.
                            }
                        }, ContextCompat.getMainExecutor(this));
                    } else {
                        // Finish the activity after the error message.
                        DialogHelper.showErrorDialog(this,
                                R.drawable.ic_warning,
                                R.string.error_camera_permission_request_finally_denied_header,
                                R.string.error_camera_permission_request_finally_denied_body,
                                true);
                    }
                });
    }

    private CameraSelector getCamera() {
        return new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();
    }

    private void finishBarcodeScanner() {
        if(cameraProvider != null) {
            cameraProvider.unbindAll();
        }
    }

    private Preview initializePreview() {
        Preview preview = new Preview.Builder()
                .build();
        preview.setSurfaceProvider(cameraPreviewView.getSurfaceProvider());

        return preview;
    }

    private ImageAnalysis initializeBarcodeScanner() {
        BarcodeScannerOptions options = new BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_ALL_FORMATS)
                .build();

        BarcodeScanner scanner = BarcodeScanning.getClient(options);

        ImageAnalysis imageAnalysis =
                new ImageAnalysis.Builder()
                        .setResolutionSelector(new ResolutionSelector.Builder().build())
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

        imageAnalysis.setAnalyzer(Executors.newSingleThreadExecutor(), imageProxy -> {
            Log.debug(TAG, "Analyzing the image!");
            @SuppressLint({"UnsafeExperimentalUsageError", "UnsafeOptInUsageError"})
            Image mediaImage = imageProxy.getImage();
            if (mediaImage != null) {
                InputImage image = InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

                // Pass image to an ML Kit Vision API...
                scanner.process(image)
                        .addOnSuccessListener(barcodeList -> {
                            Log.debug(TAG, "Successfully decoded a barcode!");
                            Log.debug(TAG, "Number of barcodeList detected: " + barcodeList.size());

                            barcodeLoop:
                            for (Barcode barcode : barcodeList) {
                                String rawValue = barcode.getRawValue();
                                Log.debug(TAG, "Raw barcode value: " + rawValue);

                                int valueType = barcode.getValueType();
                                switch (valueType) {
                                    case Barcode.TYPE_TEXT:
                                        String text = barcode.getRawValue();
                                        setBarCodeFromText(text);
                                        break barcodeLoop;
                                    case Barcode.TYPE_CALENDAR_EVENT:
                                    case Barcode.TYPE_CONTACT_INFO:
                                    case Barcode.TYPE_DRIVER_LICENSE:
                                    case Barcode.TYPE_EMAIL:
                                    case Barcode.TYPE_GEO:
                                    case Barcode.TYPE_ISBN:
                                    case Barcode.TYPE_PHONE:
                                    case Barcode.TYPE_PRODUCT:
                                    case Barcode.TYPE_SMS:
                                    case Barcode.TYPE_UNKNOWN:
                                    case Barcode.TYPE_URL:
                                    case Barcode.TYPE_WIFI:
                                    default:
                                        Log.error(TAG, "Barcode detected of wrong type: " + valueType);
                                        break;
                                }
                            }
                        })
                        .addOnFailureListener(e -> Log.error(TAG, "Failed to decode a barcode!"))
                .addOnCompleteListener(task -> imageProxy.close());
            }
        });

        return imageAnalysis;
    }

    // endregion

    // region UI manipulation

    private void attachUi() {
        Log.debug(TAG, "Attaching UI.");

        textViewCameraAvailable = findViewById(R.id.text_camera);
        cameraPreviewView = findViewById(R.id.cameraPreviewView);
        textViewBarCodeValue = findViewById(R.id.text_barcode_value);
        buttonUseThisCode = findViewById(R.id.button_use_activation_code);
        buttonUseThisCode.setOnClickListener(useThisCodeButtonClickListener);

        String readerName = viewModel.getEuiccName();
        if (readerName != null) {
            setTitle(getString(R.string.scan_qr_title) + " - " + readerName);
        }
    }

    private void setBarCodeFromText(String barcode) {
        Log.debug(TAG,"Set barcode to: " + barcode);
        textViewBarCodeValue.post(() -> {
            activationCode = new ActivationCode(barcode);
            textViewBarCodeValue.setText(activationCode.toString());

            if (activationCode.isValid()) {
                if (buttonUseThisCode != null) {
                    buttonUseThisCode.setEnabled(true);
                    buttonUseThisCode.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    // endregion

    // region Listener

    private final View.OnClickListener useThisCodeButtonClickListener = v -> {
        Log.info(TAG, "Activation code: " + activationCode);

        finishBarcodeScanner();

        // Put activation code into the intent
        Intent i = new Intent(ScanBarcodeActivity.this, DownloadActivity.class);
        i.putExtra(Application.INTENT_EXTRA_ACTIVATION_CODE, activationCode);
        startActivity(i);
    };

    // endregion
}