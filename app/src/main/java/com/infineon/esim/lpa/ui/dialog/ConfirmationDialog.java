/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;

import com.infineon.esim.lpa.R;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.data.DataModel;


public class ConfirmationDialog {

    public static void showEnableProfileConfirmationDialog(Activity parent,
                                                                    ProfileMetadata currentEnabledProfile,
                                                                    ProfileMetadata newEnabledProfile) {
        int heading;
        int buttonText;
        String bodyStr;

        String newProfileName = newEnabledProfile.getNickname();

        if (currentEnabledProfile == null) {
            heading = R.string.profile_action_dialogue_enable_profile_heading;
            buttonText = R.string.profile_details_button_enable_text;
            String bodyFormat = parent.getString(R.string.profile_action_dialogue_enable_profile_body);
            bodyStr = String.format(bodyFormat, newProfileName);
        } else {
            heading = R.string.profile_action_dialogue_switch_profile_heading;
            buttonText = R.string.profile_details_button_switch_text;
            String currentProfileName = currentEnabledProfile.getNickname();
            String bodyFormat = parent.getString(R.string.profile_action_dialogue_switch_profile_body);
            bodyStr = String.format(bodyFormat, newProfileName, currentProfileName, currentProfileName);
        }

        new AlertDialog.Builder(parent)
                .setTitle(heading)
                .setCancelable(true)
                .setMessage(bodyStr)
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss())
                .setPositiveButton(buttonText, (dialog, id) -> {
                    dialog.dismiss();
                    DataModel.getInstance().enableProfile(newEnabledProfile);
                })
                .show();
    }

    public static void showDeleteProfileConfirmationDialog(Activity parent,
                                                           ProfileMetadata deleteProfile) {

        new AlertDialog.Builder(parent)
                .setTitle(R.string.profile_action_dialogue_delete_profile_heading)
                .setCancelable(true)
                .setMessage(R.string.profile_action_dialogue_disable_delete_profile_body)
                .setNegativeButton(android.R.string.cancel, (dialog, id) -> dialog.dismiss())
                .setPositiveButton(R.string.profile_details_button_delete_text, (dialog, id) -> {
                    dialog.dismiss();
                    DataModel.getInstance().deleteProfile(deleteProfile);
                })
        .show();
    }
}
