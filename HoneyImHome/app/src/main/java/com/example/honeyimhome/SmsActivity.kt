package com.example.honeyimhome

import android.app.Application

class SmsActivity : Application() {
    override fun onCreate() {
        super.onCreate()
        //Create a class extending Application, and in it's onCreate() register a LocalSendSmsBroadcastReceiver() with intent filter that filters for action "POST_PC.ACTION_SEND_SMS" (or you can define a different string, just be consistent)
    }
}