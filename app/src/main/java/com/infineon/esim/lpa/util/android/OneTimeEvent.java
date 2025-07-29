/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.util.android;

@SuppressWarnings("unused")
public class OneTimeEvent<T> {
    private final T content;
    private boolean hasBeenHandled;

    public OneTimeEvent(T content) {
        this.content = content;
        this.hasBeenHandled = false;
    }

    public boolean hasBeenHandled() {
        return hasBeenHandled;
    }

    public T getContentIfNotHandled() {
        if(hasBeenHandled) {
            return null;
        } else {
            hasBeenHandled = true;
            return content;
        }
    }

    public T peekContent() {
        return content;
    }
}
