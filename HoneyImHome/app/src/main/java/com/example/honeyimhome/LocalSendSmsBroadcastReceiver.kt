package com.example.honeyimhome
//
//import android.Manifest.permission.SEND_SMS
//import android.app.Activity
//import android.content.BroadcastReceiver
//import android.content.Context
//import android.content.Intent
//import android.content.pm.PackageManager
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import androidx.core.content.ContextCompat.*
//import com.example.honeyimhome.PermissionHandler.Companion.checkPermissions
//import java.util.jar.Manifest
//@Suppress("UNREACHABLE_CODE")
//class LocalSendSmsBroadcastReceiver : BroadcastReceiver() {
//    override fun onReceive(context: Context, intent: Intent?) {
//        TODO("Not yet implemented")
//        //check that we have the send-sms runtime permission. If not, log an error to the logcat and return
//        //extract the phone number and the sms-message from the intent's extras using the 2 keys. if one of them is null or empty, log an error and return.
//        //use android's SmsManager to send the sms.
//        //create a push notification showing this text: "sending sms to <phone number>: <text message>"
//        if (checkPermissions(context, SEND_SMS)){
//            sendSms()
//        }else{
//            ActivityCompat.requestPermissions(
//                context as Activity, arrayOf(SEND_SMS),
//                REQUEST_CODE_PERMISSION_SMS)
//        }
//    }
//    fun sendSms(){
//
//    }
//    companion object {
//        private const val REQUEST_CODE_PERMISSION_SMS = 1546
//    }
//}