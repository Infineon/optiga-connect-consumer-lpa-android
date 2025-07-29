/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */
package com.infineon.esim.lpa.ui.profileList;

import android.app.AlertDialog;
import android.content.Intent;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.oss.licenses.OssLicensesMenuActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.infineon.esim.lpa.BuildConfig;
import com.infineon.esim.lpa.R;
import com.infineon.esim.lpa.data.Preferences;
import com.infineon.esim.lpa.ui.enterActivationCode.EnterActivationCodeActivity;
import com.infineon.esim.lpa.ui.euiccDetails.EuiccDetailsActivity;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;
import com.infineon.esim.lpa.ui.generic.Error;
import com.infineon.esim.lpa.ui.preference.PreferenceActivity;
import com.infineon.esim.lpa.ui.scanBarcode.ScanBarcodeActivity;
import com.infineon.esim.lpa.util.android.DialogHelper;
import com.infineon.esim.lpa.util.android.EventObserver;
import com.infineon.esim.lpa.util.android.NetworkStatus;
import com.infineon.esim.lpa.util.android.WifiStatus;
import com.infineon.esim.util.Log;

final public class ProfileListActivity extends AppCompatActivity {
    private static final String TAG = ProfileListActivity.class.getName();

    private Boolean allowBackButtonPress = false;

    private AlertDialog progressDialog;

    private ProfileListViewModel viewModel;

    private boolean isAllFabsVisible;

    private FloatingActionButton fab;

    private FloatingActionButton fabCamera;

    private FloatingActionButton fabManual;

    private TextView addProfileCameraText;

    private TextView addProfileManualText;

    // region Lifecycle Management

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.debug(TAG, "Created activity.");

        getOnBackPressedDispatcher().addCallback(onBackPressedCallback);

        Log.debug(TAG, "Setting live data observer.");
        // Get the ViewModel
        viewModel = new ViewModelProvider(this).get(ProfileListViewModel.class);
        viewModel.getEuiccNameLiveData().observe(this, euiccNameObserver);
        viewModel.getActionStatusLiveData().observe(this, actionStatusObserver);
        viewModel.getError().observe(this, errorEventObserver);

        // Attach the UI
        attachUi();

        // Initialize USB reader if app has been started from USB attach event
        UsbDevice usbDevice;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            usbDevice = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE, UsbDevice.class);
        } else {
            usbDevice = getIntent().getParcelableExtra(UsbManager.EXTRA_DEVICE);
        }

        if(usbDevice != null) {
            viewModel.connectIdentiveEuiccInterface();
        }
    }

    final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            if(allowBackButtonPress) {
                Log.debug(TAG, "Processing backpress.");
                finish();
            } else {
                Log.debug(TAG, "Ignoring backpress.");
            }
        }
    };

    @Override
    protected void onPause() {
        Log.debug(TAG, "Pausing activity.");

        // Stop observing errors when activity is not visible
        viewModel.getError().removeObserver(errorEventObserver);

        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.debug(TAG, "Resuming activity.");

        // Start observing errors when activity is visible again
        viewModel.getError().observe(this, errorEventObserver);

        // Initialize freshly attached USB reader
        viewModel.selectFreshlyAttachedUsbReader();

        hideButtons();

        super.onResume();
    }

    @Override
    protected void onDestroy() {
        Log.debug(TAG, "Destroying activity.");

        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_profile_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        // Use if instead of switch because of this warning:
        // Resource IDs will be non-final in Android Gradle Plugin version 5.0, avoid using them in
        // switch case statements
        int id = item.getItemId();
        if(id == R.id.action_settings) {
            // Open settings intent
            startActivity(new Intent(this, PreferenceActivity.class));
            return true;
        } else if(id == R.id.action_app_version) {
            // Show app version popup
            showAppVersionPopup();
            return true;
        } else if(id == R.id.action_license_info) {
            // Start license info intent
            showOpenSourceLicenseActivity();
            return true;
        } else if(id == R.id.action_euicc_info) {
            // Start eUICC info intent
            startActivity(new Intent(this, EuiccDetailsActivity.class));
            return true;
        } else if(id == R.id.action_clear_notifications) {
            // Clear all eUICC notifications
            viewModel.clearAllNotifications();
            return true;
        } else if(id == R.id.action_refresh_esims) {
            // Start eUICC info intent
            viewModel.refreshEuiccs();
            return true;
        } else if(id == R.id.action_refresh_profile_list) {
            // Start eUICC info intent
            viewModel.refreshProfileList();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    // endregion

    // region UI manipulation

    private void hideButtons()
    {
        fabCamera.setVisibility(View.GONE);
        fabManual.setVisibility(View.GONE);
        addProfileCameraText.setVisibility(View.GONE);
        addProfileManualText.setVisibility(View.GONE);

        isAllFabsVisible = false;
    }

    private void showButtons()
    {
        fabCamera.setVisibility(View.VISIBLE);
        fabManual.setVisibility(View.VISIBLE);
        addProfileCameraText.setVisibility(View.VISIBLE);
        addProfileManualText.setVisibility(View.VISIBLE);

        isAllFabsVisible = true;
    }

    private void attachUi() {
        Log.debug(TAG, "Attaching UI.");
        setContentView(R.layout.activity_profile_list);
        setSupportActionBar(findViewById(R.id.toolbar));

        fab = findViewById(R.id.button_add_profile);
        fab.setOnClickListener(floatingButtonOnClickListener);

        fabCamera = findViewById(R.id.button_add_profile_camera);
        fabCamera.setOnClickListener(floatingButtonCameraOnClickListener);

        fabManual = findViewById(R.id.button_add_profile_manual);
        fabManual.setOnClickListener(floatingButtonManualOnClickListener);

        // Also register the action name text, of all the FABs.
        addProfileCameraText = findViewById(R.id.add_profile_camera_text);
        addProfileManualText = findViewById(R.id.add_profile_manual_text);

        // Now set all the FABs and all the action name texts as GONE
        hideButtons();
        setProfileListFragment();
    }

    private void setProfileListFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.profile_list_placeholder, new ProfileListFragment());
        fragmentTransaction.commit();
    }

    private void setNoReaderFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.replace(R.id.profile_list_placeholder, new NoReaderFragment());
        fragmentTransaction.commit();
    }

    private void allowBackButtonPress() {
        allowBackButtonPress = true;
    }

    private void disallowBackButtonPress() {
        allowBackButtonPress = false;
    }

    private void showOpenSourceLicenseActivity() {
        startActivity(new Intent(this, OssLicensesMenuActivity.class));
    }

    private void showAppVersionPopup() {
        String body = String.format(getString(R.string.app_version_body),
                BuildConfig.APPLICATION_ID,
                BuildConfig.BUILD_TYPE,
                BuildConfig.VERSION_NAME);

        Log.debug(TAG,"Showing app version info: " + body);
        DialogHelper.showErrorDialog(this,
                R.string.menu_item_app_version,
                body,
                false);
    }

    // endregion

    // region Listener and observer
    final View.OnClickListener floatingButtonOnClickListener = (view) -> {
        if(NetworkStatus.isNetworkAvailable() && WifiStatus.isWifiEnabled()) {
            if (!isAllFabsVisible) {
                showButtons();
            } else {
                hideButtons();
            }
        } else {
            if (!WifiStatus.isWifiEnabled()) {
                new AlertDialog.Builder(this)
                        .setTitle(R.string.error_wifi_disabled_heading)
                        .setMessage(R.string.error_wifi_disabled_body)
                        .setCancelable(true)
                        .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss())
                        .setNeutralButton(R.string.error_wifi_disabled_positive_button, (dialog, id) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                        .show();
            } else {
                if (!NetworkStatus.isNetworkAvailable()) {
                    new AlertDialog.Builder(this)
                            .setTitle(R.string.error_wifi_not_connected_heading)
                            .setMessage(R.string.error_wifi_not_connected_body)
                            .setCancelable(true)
                            .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss())
                            .setNeutralButton(R.string.error_wifi_not_connected_positive_button, (dialog, id) -> startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)))
                            .show();
                }
            }
        }
    };

    final View.OnClickListener floatingButtonCameraOnClickListener = (view ->
            startActivity(new Intent(ProfileListActivity.this, ScanBarcodeActivity.class)));


    final View.OnClickListener floatingButtonManualOnClickListener = (view ->
        startActivity(new Intent(ProfileListActivity.this, EnterActivationCodeActivity.class)));


    final Observer<String> euiccNameObserver = euiccName -> {

        if(euiccName.equals(Preferences.getNoEuiccName())) {
            Log.error(TAG, "No eSIM reader available!");

            setTitle(getString(R.string.app_name) + " - " + "No eSIM available!");

            setNoReaderFragment();
        } else {
            Log.debug(TAG, "Observed change in eUICC name: " + euiccName);

            setTitle(getString(R.string.app_name) + " - " + euiccName);

            setProfileListFragment();
        }
    };

    final Observer<AsyncActionStatus> actionStatusObserver = actionStatus -> {
        Log.debug(TAG, "Observed that action status changed: " + actionStatus.getActionStatus());

        // Dismiss the last dialog if there is any
        dismissProgressDialog();

        switch (actionStatus.getActionStatus()) {
            case REFRESHING_EUICC_LIST_STARTED: {
                progressDialog = DialogHelper.showProgressDialog(this, R.string.pref_progress_refreshing_euicc_list);
                disallowBackButtonPress();
                break;
            }
            case OPENING_EUICC_CONNECTION_STARTED: {
                String euiccName = (String) actionStatus.getExtras();
                if(euiccName != null) {
                    String body = String.format(getString(R.string.action_opening_reader), euiccName);
                    progressDialog = DialogHelper.showProgressDialog(this, body);
                    disallowBackButtonPress();
                }
                break;
            }
            case GET_PROFILE_LIST_STARTED: {
                progressDialog = DialogHelper.showProgressDialog(this, R.string.action_getting_profiles);
                disallowBackButtonPress();
                break;
            }
            case ENABLE_PROFILE_STARTED: {
                progressDialog = DialogHelper.showProgressDialog(this, R.string.action_switching_profile);
                disallowBackButtonPress();
                break;
            }
            case CLEAR_ALL_NOTIFICATIONS_STARTED: {
                progressDialog = DialogHelper.showProgressDialog(this, R.string.action_clearing_notifications);
                disallowBackButtonPress();
                break;
            }
            case REFRESHING_EUICC_LIST_FINISHED:
            case OPENING_EUICC_CONNECTION_FINISHED:
            case GET_PROFILE_LIST_FINISHED:
            case ENABLE_PROFILE_FINISHED:
            case CLEAR_ALL_NOTIFICATIONS_FINISHED: {
                allowBackButtonPress();
                break;
            }
            default:
                // nothing
        }
    };

    private void dismissProgressDialog() {
        if (progressDialog != null) progressDialog.dismiss();
    }

    final EventObserver<Error> errorEventObserver = new EventObserver<>(error -> {
        Log.debug(TAG, "Observed that error happened during loading: " + error);
        dismissProgressDialog();
        DialogHelper.showErrorDialog(this, error, false);
        allowBackButtonPress();
    });
}
