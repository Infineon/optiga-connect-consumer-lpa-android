/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Infineon Technologies AG
 * SPDX-License-Identifier: MIT
 */
package com.infineon.esim.util;

final public class Log {

    // Ref:
    // https://stackoverflow.com/questions/8355632/how-do-you-usually-tag-log-entries-android
    public static String getFileLineNumber() {
        String info = "";
        final StackTraceElement[] ste = Thread.currentThread().getStackTrace();
        for (int i = 0; i < ste.length; i++) {
            if (ste[i].getMethodName().equals("getFileLineNumber")) {
                info = "("+ste[i + 1].getFileName() + ":" + ste[i + 1].getLineNumber()+")";
            }
        }
        return info;
    }

    public static void verbose(final String tag, final String msg) {
        android.util.Log.v(tag, msg);
    }

    public static void debug(final String tag, final String msg) {
        android.util.Log.d(tag, msg);
    }

    public static void info(final String tag, final String msg) {
        android.util.Log.i(tag, msg);
    }

    public static void warn(final String tag, final String msg) {
        android.util.Log.w(tag, msg);
    }

    public static void warn(final String tag, final String msg, final Throwable error) {
        android.util.Log.w(tag, msg, error);
    }

    public static void error(final String msg) {
        android.util.Log.e("", msg);
    }

    public static void error(final String tag, final String msg) {
        android.util.Log.e(tag, msg);
    }

    public static void error(final String tag, final String msg, final Throwable error) {
        android.util.Log.e(tag, msg, error);
    }
}
