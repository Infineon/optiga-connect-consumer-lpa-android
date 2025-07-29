/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */

package com.infineon.esim.lpa.util.threading;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskRunner {
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private final Handler handler = new Handler(Looper.getMainLooper());

    public interface Callback<R> {
        void onComplete(R result);
    }

    public interface ExceptionHandler {
        void onException(Exception e);
    }

    public <R> void executeAsync(Callable<R> callable, Callback<R> callback, ExceptionHandler exceptionHandler) {
        executorService.submit(() -> {
            final R result;
            try {
                result = callable.call();
                handler.post(() -> callback.onComplete(result));
            } catch (Exception e) {
                handler.post(() -> exceptionHandler.onException(e));
            }
        });
    }
}