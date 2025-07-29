/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core;

import com.infineon.esim.lpa.core.dtos.ActivationCode;
import com.infineon.esim.lpa.core.dtos.EuiccInfo;
import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.core.dtos.result.GenericOperationResult;
import com.infineon.esim.lpa.core.dtos.result.local.ClearNotificationsResult;
import com.infineon.esim.lpa.core.dtos.result.local.DeleteResult;
import com.infineon.esim.lpa.core.dtos.result.local.DisableResult;
import com.infineon.esim.lpa.core.dtos.result.local.EnableResult;
import com.infineon.esim.lpa.core.dtos.result.local.SetNicknameResult;
import com.infineon.esim.lpa.core.dtos.result.remote.AuthenticateResult;
import com.infineon.esim.lpa.core.dtos.result.remote.CancelSessionResult;
import com.infineon.esim.lpa.core.dtos.result.remote.DownloadResult;
import com.infineon.esim.lpa.core.dtos.result.remote.HandleNotificationsResult;

import java.util.List;

public interface LocalProfileAssistantCore {
    // Profile management
    String getEID() throws Exception;
    EuiccInfo getEuiccInfo2() throws Exception;
    List<ProfileMetadata> getProfiles() throws Exception;

    // Profile operations
    EnableResult enableProfile(String iccid, boolean refreshFlag) throws Exception;
    DisableResult disableProfile(String iccid) throws Exception;
    DeleteResult deleteProfile(String iccid) throws Exception;
    SetNicknameResult setNickname(String iccid, String nickname) throws Exception;

    // Profile download
    AuthenticateResult authenticate(ActivationCode activationCode) throws Exception;
    DownloadResult downloadProfile(String confirmationCode) throws Exception;
    CancelSessionResult cancelSession(long cancelSessionReasonValue) throws Exception;

    // Notification management
    HandleNotificationsResult handleNotifications() throws Exception;
    ClearNotificationsResult clearPendingNotifications() throws Exception;

    // Get details about last error
    GenericOperationResult getLastError();
}