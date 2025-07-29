/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.util.android;

import androidx.lifecycle.Observer;

import com.infineon.esim.lpa.ui.profileDetails.ProfileDetailsActivity;
import com.infineon.esim.util.Log;

public class EventObserver<T> implements Observer<OneTimeEvent<T>> {
    private static final String TAG = ProfileDetailsActivity.class.getName();

    private final EventHandler<T> eventHandler;

    public EventObserver(EventHandler<T> eventHandler) {
        this.eventHandler = eventHandler;
    }

    @Override
    public void onChanged(OneTimeEvent<T> oneTimeEvent) {
        Log.debug(TAG,"Received an one-time event: " + oneTimeEvent);
        if(oneTimeEvent.hasBeenHandled()) {
            Log.debug(TAG,"The one-time event has already been handled. Nothing to do.");
        } else {
            Log.debug(TAG,"Handling the one-time event.");
            eventHandler.handleEvent(oneTimeEvent.getContentIfNotHandled());
        }
    }

    public interface EventHandler<T> {
        void handleEvent(T t);
    }
}
