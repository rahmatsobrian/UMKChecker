package com.rahmatsobrian.umkchecker.util

import android.util.Log
import com.rahmatsobrian.umkchecker.BuildConfig

/**
 * Thin logging wrapper that is completely inert on release builds.
 * Using this instead of [android.util.Log] directly guarantees no debug
 * information ever leaks into a production build, even if ProGuard/R8
 * stripping is somehow bypassed.
 */
object AppLogger {

    private const val DEFAULT_TAG = "UMKChecker"

    fun d(tag: String = DEFAULT_TAG, message: String) {
        if (BuildConfig.DEBUG) Log.d(tag, message)
    }

    fun e(tag: String = DEFAULT_TAG, message: String, throwable: Throwable? = null) {
        if (BuildConfig.DEBUG) Log.e(tag, message, throwable)
    }

    fun i(tag: String = DEFAULT_TAG, message: String) {
        if (BuildConfig.DEBUG) Log.i(tag, message)
    }
}
