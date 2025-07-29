/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.ui.generic;

@SuppressWarnings("unused")
public class AsyncActionStatus {
    private final ActionStatus actionStatus;
    private Object extras;

    public AsyncActionStatus(ActionStatus actionStatus) {
        this.actionStatus = actionStatus;
    }

    public ActionStatus getActionStatus() {
        return actionStatus;
    }

    public Object getExtras() {
        return extras;
    }

    public void setExtras(Object extras) {
        this.extras = extras;
    }

    public AsyncActionStatus addExtras(Object extras) {
        this.extras = extras;
        return this;
    }

    public boolean isBusy() {
        switch (actionStatus) {
            case GETTING_EUICC_INFO_STARTED:
            case REFRESHING_EUICC_LIST_STARTED:
            case CONNECTING_INTERFACE_STARTED:
            case DISCONNECTING_INTERFACE_STARTED:
            case OPENING_EUICC_CONNECTION_STARTED:
            case GET_PROFILE_LIST_STARTED:
            case ENABLE_PROFILE_STARTED:
            case DISABLE_PROFILE_STARTED:
            case DELETE_PROFILE_STARTED:
            case SET_NICKNAME_STARTED:
            case AUTHENTICATE_DOWNLOAD_STARTED:
            case DOWNLOAD_PROFILE_STARTED:
            case CANCEL_SESSION_STARTED: return true;
            case GETTING_EUICC_INFO_FINISHED:
            case REFRESHING_EUICC_LIST_FINISHED:
            case CONNECTING_INTERFACE_CANCELLED:
            case CONNECTING_INTERFACE_FINISHED:
            case DISCONNECTING_INTERFACE_FINISHED:
            case OPENING_EUICC_CONNECTION_FINISHED:
            case GET_PROFILE_LIST_FINISHED:
            case ENABLE_PROFILE_FINISHED:
            case DISABLE_PROFILE_FINISHED:
            case DELETE_PROFILE_FINISHED:
            case AUTHENTICATE_DOWNLOAD_FINISHED:
            case DOWNLOAD_PROFILE_FINISHED:
            case CANCEL_SESSION_FINISHED:
            default: return false;
        }
    }
}
