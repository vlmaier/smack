package org.vmaier.smack.controller

import android.app.Application
import org.vmaier.smack.util.SharedPreferences


class App : Application() {

    companion object {
        lateinit var prefs: SharedPreferences
    }

    override fun onCreate() {
        prefs = SharedPreferences(applicationContext)
        super.onCreate()
    }
}