package com.rahmatsobrian.umkchecker

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Application entry point. Annotated with [HiltAndroidApp] to trigger Hilt's
 * code generation and set up the dependency graph for the whole app.
 */
@HiltAndroidApp
class UmkCheckerApp : Application()
