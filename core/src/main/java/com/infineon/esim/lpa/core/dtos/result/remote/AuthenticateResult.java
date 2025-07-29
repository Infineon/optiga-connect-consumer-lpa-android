/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos.result.remote;

import com.infineon.esim.lpa.core.dtos.profile.ProfileMetadata;
import com.infineon.esim.lpa.core.dtos.result.GenericOperationResult;

public class AuthenticateResult extends GenericOperationResult {
    private final Boolean isCcRequired;
    private final ProfileMetadata profileMetadata;

    public AuthenticateResult(Boolean isCcRequired, ProfileMetadata profileMetadata) {
        super();
        this.isCcRequired = isCcRequired;
        this.profileMetadata = profileMetadata;
    }

    public AuthenticateResult(GenericOperationResult genericOperationResult) {
        super(genericOperationResult);
        this.isCcRequired = null;
        this.profileMetadata = null;
    }

    public AuthenticateResult(RemoteError remoteError) {
        super(remoteError);
        this.isCcRequired = null;
        this.profileMetadata = null;
    }

    public Boolean getCcRequired() {
        return isCcRequired;
    }

    public ProfileMetadata getProfileMetadata() {
        return profileMetadata;
    }
}
