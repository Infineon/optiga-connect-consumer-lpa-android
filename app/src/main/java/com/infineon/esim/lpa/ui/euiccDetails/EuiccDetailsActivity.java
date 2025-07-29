/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.euiccDetails;

import android.app.AlertDialog;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.infineon.esim.lpa.R;
import com.infineon.esim.lpa.core.dtos.EuiccInfo;
import com.infineon.esim.lpa.ui.generic.AsyncActionStatus;
import com.infineon.esim.lpa.util.android.DialogHelper;
import com.infineon.esim.util.Log;

public class EuiccDetailsActivity extends AppCompatActivity {
    private static final String TAG = EuiccDetailsActivity.class.getName();

    private EuiccDetailsViewModel viewModel;

    private TextView textViewEid;
    private TextView textViewGsmaVersion;
    private TextView textViewTcaVersion;
    private TextView textViewPkiIds;

    private AlertDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_euicc_details);
        Log.debug(TAG, "Created activity.");

        // Get the view model
        viewModel = new ViewModelProvider(this).get(EuiccDetailsViewModel.class);
        viewModel.getActionStatus().observe(this, actionStatusObserver);

        // Attach UI
        attachUi();

        // Refreshing eUICC info
        viewModel.refreshEuiccInfo();
    }

    @Override
    protected void onPause() {
        Log.debug(TAG, "onPause");
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.debug(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onStop() {
        // App crashes on Oppo Reno Z when starting with Android studio if not used...
        super.onStop();
        finish();
    }

    private void attachUi() {
        textViewEid = findViewById(R.id.text_eid);
        textViewGsmaVersion = findViewById(R.id.text_gsma_version);
        textViewTcaVersion = findViewById(R.id.text_tca_version);
        textViewPkiIds = findViewById(R.id.text_pki_ids);

        textViewEid.setText("");
        textViewGsmaVersion.setText("");
        textViewTcaVersion.setText("");
        textViewPkiIds.setText("");
    }

    private void setEuiccInfo(EuiccInfo euiccInfo) {
        textViewEid.setText(euiccInfo.getEid());
        textViewGsmaVersion.setText(euiccInfo.getSvn());
        textViewTcaVersion.setText(euiccInfo.getProfileVersion());
        textViewPkiIds.setText(euiccInfo.getPkiIdsAsString());
    }

    private void dismissProgressDialog() {
        if(progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    final Observer<AsyncActionStatus> actionStatusObserver = actionStatus -> {
        Log.debug(TAG, "Observed that action status changed: " + actionStatus);
        dismissProgressDialog();

        switch (actionStatus.getActionStatus()) {
            case GETTING_EUICC_INFO_STARTED:
                // Getting eUICC info is too fast for a progress dialog
                progressDialog = DialogHelper.showProgressDialog(this, R.string.euicc_info_progress_getting_euicc_info);
                break;
            case GETTING_EUICC_INFO_FINISHED:
                setEuiccInfo(viewModel.getEuiccInfo());
                break;
            default:
                // nothing
        }
    };
}
