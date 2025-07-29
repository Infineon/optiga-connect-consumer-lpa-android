/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */
package com.infineon.esim.lpa.ui.preference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.preference.CheckBoxPreference;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import com.infineon.esim.lpa.Application;
import com.infineon.esim.lpa.R;
import com.infineon.esim.lpa.core.es9plus.TlsUtil;
import com.infineon.esim.util.Log;

import java.util.List;

public class PreferenceFragment extends PreferenceFragmentCompat {
    private static final String TAG = PreferenceFragment.class.getName();

    private PreferenceViewModel viewModel;

    // region Lifecycle management

    @NonNull
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PreferenceActivity activity = (PreferenceActivity) requireActivity();

        viewModel.getEuiccListLiveData().observe(activity, euiccListObserver);
        viewModel.getCurrentEuiccLiveData().observe(activity, currentEuiccObserver);

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        viewModel = new ViewModelProvider(this).get(PreferenceViewModel.class);

        setPreferencesFromResource(R.xml.preferences, rootKey);

        CheckBoxPreference checkBoxPreferenceTrustTestCi = findPreference(getString(R.string.pref_key_trust_gsma_test_ci));
        if(checkBoxPreferenceTrustTestCi != null) {
            checkBoxPreferenceTrustTestCi.setOnPreferenceChangeListener(trustTestCiChangeListener);
        }
    }

    // endregion

    // region UI manipulation

    public void updateEuiccList() {
        List<String> euiccNames = viewModel.getEuiccList();
        String currentEuicc = viewModel.getCurrentEuicc();

        Log.debug(TAG, "Euicc list: current eUICC: " + currentEuicc);
        Log.debug(TAG, "Euicc list: " + euiccNames);

        ListPreference readerPref = findPreference(Application.getStringResource(R.string.pref_key_select_se));
        if(readerPref != null) {
            readerPref.setEntries(euiccNames.toArray(new String[0]));
            readerPref.setEntryValues(euiccNames.toArray(new String[0]));
            if((currentEuicc != null) && euiccNames.contains(currentEuicc)) {
                readerPref.setValue(currentEuicc);
            }
        }
    }

    final Preference.OnPreferenceChangeListener trustTestCiChangeListener = (preference, newValue) -> {
        Boolean trustTestCi = (Boolean) newValue;

        TlsUtil.setTrustLevel(trustTestCi);

        return true;
    };

    final Observer<List<String>> euiccListObserver = euiccList -> {
        Log.debug(TAG, "Observed change in eUICC list: " + euiccList);
        updateEuiccList();
    };

    final Observer<String> currentEuiccObserver = euiccName -> {
        Log.debug(TAG, "Observed change in eUICC: " + euiccName);
        updateEuiccList();
    };

    // endregion
}