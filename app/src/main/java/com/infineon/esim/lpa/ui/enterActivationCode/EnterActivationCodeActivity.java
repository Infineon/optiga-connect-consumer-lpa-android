/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.enterActivationCode;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.infineon.esim.lpa.Application;
import com.infineon.esim.lpa.R;
import com.infineon.esim.lpa.core.dtos.ActivationCode;
import com.infineon.esim.lpa.ui.downloadProfile.DownloadActivity;
import com.infineon.esim.util.Log;

public class EnterActivationCodeActivity extends AppCompatActivity {
    private final static String TAG = com.infineon.esim.lpa.ui.enterActivationCode.EnterActivationCodeActivity.class.getName();

    private EditText textEditActivationCodeUrl;
    private EditText textEditActivationCodeID;
    private EditText textEditActivationCodeAC;
    private ActivationCode activationCode;
    private Button buttonUseThisCode;

    private EnterActivationCodeViewModel viewModel;


    // region Lifecycle management

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_activation_code);

        this.viewModel = new ViewModelProvider(this).get(EnterActivationCodeViewModel.class);

        // Attach the UI
        attachUi();
    }

    @Override
    protected void onResume() {
        Log.debug(TAG,"Resuming activity.");
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.debug(TAG,"Pausing activity.");
        super.onPause();
    }

    @Override
    public void onStop() {
        // App crashes on Oppo Reno Z when starting with Android studio if not used...
        super.onStop();
        finish();
    }

    // endregion


    // region UI manipulation

    private void attachUi() {
        Log.debug(TAG, "Attaching UI.");

        textEditActivationCodeUrl = findViewById(R.id.edit_activation_code_url);
        textEditActivationCodeID = findViewById(R.id.edit_activation_code_id);
        textEditActivationCodeAC = findViewById(R.id.edit_activation_code_ac);
        buttonUseThisCode = findViewById(R.id.button_use_manual_activation_code);
        buttonUseThisCode.setOnClickListener(useThisCodeButtonClickListener);

        String readerName = viewModel.getEuiccName();
        if (readerName != null) {
            setTitle(getString(R.string.scan_qr_title) + " - " + readerName);
        }
    }

    // endregion

    // region Listener

    private final View.OnClickListener useThisCodeButtonClickListener = v -> {
        Log.info(TAG, "Enter Activation code.");

        activationCode = new ActivationCode(textEditActivationCodeAC.getText().toString(), textEditActivationCodeUrl.getText().toString(),
                            textEditActivationCodeID.getText().toString());

        // Put activation code into the intent
        Intent i = new Intent(com.infineon.esim.lpa.ui.enterActivationCode.EnterActivationCodeActivity.this, DownloadActivity.class);
        i.putExtra(Application.INTENT_EXTRA_ACTIVATION_CODE, activationCode);
        startActivity(i);
    };

    // endregion
}