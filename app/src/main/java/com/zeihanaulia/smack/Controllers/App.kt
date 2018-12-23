package com.zeihanaulia.smack.Controllers

import android.app.Application
import com.zeihanaulia.smack.Utilities.SharedPrefs

class App: Application() {

    companion object {
        lateinit var perfs: SharedPrefs
    }

    override fun onCreate() {
        perfs = SharedPrefs(applicationContext)
        super.onCreate()
    }
}