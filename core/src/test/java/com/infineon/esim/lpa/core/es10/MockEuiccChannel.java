/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es10;


import java.util.ArrayList;
import java.util.List;

public class MockEuiccChannel implements EuiccChannel {

    private final List<String> requestApdus;
    private final List<String> responseApdus;

    public MockEuiccChannel() {
        this.requestApdus = new ArrayList<>();
        this.responseApdus = new ArrayList<>();
    }

    public List<String> getRequestApdus() {
        return requestApdus;
    }

    public void setResponseApdus(List<String> responseApdus) {
        this.responseApdus.addAll(responseApdus);
    }

    @Override
    public List<String> transmitAPDUS(List<String> apdus) {
        this.requestApdus.addAll(apdus);

        return responseApdus;
    }
}
