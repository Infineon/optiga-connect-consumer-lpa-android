/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.dtos;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

public class ActivationCodeTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "LPA:1$SMDP.GSMA.COM$04386-AGYFT-A74Y8-3F815",
            "LPA:1$SMDP.GSMA.COM$04386-AGYFT-A74Y8-3F815$$1",
            "LPA:1$SMDP.GSMA.COM$04386-AGYFT-A74Y8-3F815$1.3.6.1.4.1.31746$1",
            "LPA:1$SMDP.GSMA.COM$04386-AGYFT-A74Y8-3F815$1.3.6.1.4.1.31746",
            //"LPA:1$SMDP.GSMA.COM$$1.3.6.1.4.1.31746", // This case from the GSMA SGP.22 spec is not supported
            "LPA:1$testsmdpplus.infineon.com$0000-0000-0000-0001",
            "LPA:1$testsmdpplus.infineon.com$0000-0000-0000-0001$2.999.10",
            "LPA:1$testsmdpplus.infineon.com$0000-0000-0000-0001$2.999.10$1",
            "LPA:1$testsmdpplus.infineon.com$0000-0000-0000-0001$$1",
            "LPA:1$raspberrypi.fritz.box:4443$0000-0000-0000-0001",
            "LPA:1$raspberrypi.fritz.box:4443$0000-0000-0000-0001$2.999.10",
            "LPA:1$raspberrypi.fritz.box:4443$0000-0000-0000-0001$2.999.10$1",
            "LPA:1$raspberrypi.fritz.box:4443$0000-0000-0000-0001$$1"
    })
    public void validActivationCodes(String barcode) {
        ActivationCode activationCode = new ActivationCode(barcode);

        assertTrue(activationCode.isValid());
    }

    @ParameterizedTest
    @ValueSource(strings = {
            "LPA:2$testsmdpplus.infineon.com$0000-0000-0000-0001",
            "LPA:1$testsmdpplus.infineon.com$0000-0000-0000-0001$$$1",
            "LPA:1$testsmdpplus.infineon.com$",
            "LPA:1$$$",
            "LPA:1$$",
            "$$",
            "LPA:1$testsmdpplus.infineon.com",
            "LPA:2$testsmdpplus.infineon.com:4443$0000-0000-0000-0001",
            "LPA:1$testsmdpplus.infineon.com:4443$0000-0000-0000-0001$$$1",
            "LPA:1$testsmdpplus.infineon.com:4443$",
            "LPA:1$testsmdpplus.infineon.com:4443"
    })
    public void invalidActivationCodes(String barcode) {
        ActivationCode activationCode = new ActivationCode(barcode);

        assertFalse(activationCode.isValid());
    }
}