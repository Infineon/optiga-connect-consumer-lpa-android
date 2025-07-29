/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.core.es10.definitions;

import com.gsma.sgp.messages.rspdefinitions.NotificationEvent;

public class NotificationEvents {
    public static final NotificationEvent ALL = new NotificationEvent(new boolean[] {true, true, true, true});
    public static final NotificationEvent INSTALL = new NotificationEvent(new boolean[] {true, false, false, false});
    public static final NotificationEvent ENABLE = new NotificationEvent(new boolean[] {false, true, false, false});
    public static final NotificationEvent DISABLE = new NotificationEvent(new boolean[] {false, false, true, false});
    public static final NotificationEvent DELETE = new NotificationEvent(new boolean[] {false, false, false, true});
}
