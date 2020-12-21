package com.example.aidl.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class LocalService : Service() {

    private val TAG = "LocalService"

    override fun onCreate() {
        Log.e(TAG , "execute onCreate ")
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG , "execute onStartCommand ")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG , "execute onBind ")
        return null
    }

    override fun onDestroy() {
        Log.e(TAG , "execute onDestroy ")
        super.onDestroy()
    }
}