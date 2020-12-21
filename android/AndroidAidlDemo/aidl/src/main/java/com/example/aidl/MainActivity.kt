package com.example.aidl

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.widget.Toast
import com.example.aidl.service.LocalService
import com.example.androidaidldemo.IMyAidlInterface
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"

    private lateinit var serviceAidlInterface: IMyAidlInterface

    val connection = object : ServiceConnection{


        override fun onServiceDisconnected(name: ComponentName?) {

        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            serviceAidlInterface = IMyAidlInterface.Stub.asInterface(service)
        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 启动服务
        start_service.setOnClickListener {
            Log.e(TAG , "start_service click")
            val intent = Intent()
//            val intent = Intent(this , LocalService::class.java)
            intent.setClassName("com.example.aidl.service",
                    "com.example.aidl.service.LocalService")
//            val intent = Intent(this , LocalService::class.java)
            intent.action = "com.example.aidl.service.LocalService"
            intent.setPackage("com.example.aidl.service")
//            intent.setClassName("com.example.androidaidldemo.service",
//                    "com.example.androidaidldemo.service")

            startService(intent)
        }

        aidl_test.setOnClickListener {
            val name = serviceAidlInterface.name()
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show()
        }


    }
}