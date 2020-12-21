package com.example.androidaidldemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import com.example.androidaidldemo.IMyAidlInterface

class MyService : Service(){

    private val TAG = "MyService"

     private val serviceAidlInterface = object : IMyAidlInterface.Stub(){
         override fun name(): String {
             return "my name is aidl service"
         }
     }

    override fun onCreate() {
        Log.e(TAG , "MyService is onCreate")
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        Log.e(TAG , "MyService is onBind")
        return serviceAidlInterface.asBinder()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.e(TAG , "MyService is onStartCommand")
        return super.onStartCommand(intent, flags, startId)
    }

}