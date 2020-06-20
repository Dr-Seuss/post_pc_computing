package com.example.honeyimhome

import android.Manifest
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.telephony.SmsManager
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.example.honeyimhome.MsgHandler.Companion.ACTION_SEND_SMS
import com.example.honeyimhome.MsgHandler.Companion.CHANNEL_ID
import java.text.SimpleDateFormat
import java.util.*

class LocalSendSmsBroadcastReceiver : BroadcastReceiver() {
    companion object {
        private const val INTENT_TAG = "INTENT"
        private const val EMPTY_VAL = "empty val"
        private const val NOTIFICATION_TITLE = "sms message"
        const val PHONE_NUMBER_INTENT = "PHONE"
        const val SMS_MESSAGE_INTENT = "CONTENT"
        private const val SMS_PERMISSON_TAG = "SMS PERMISSION"
        private const val NO_SMS_PERMISSION = "no permission"
    }

    override fun onReceive(context: Context, intent: Intent) {
        //check that we have the send-sms runtime permission.
        // If not, log an error to the logcat and return
        if (!checkPermissions(context)) {
            Log.e("NO PERMISSION",intent.toString())
            return
        }

        val action = intent.action

        //if one of them is null or empty, log an error and return.
        if (action == null) {
            Log.e(INTENT_TAG, EMPTY_VAL)
            return
        }

        else if (action == ACTION_SEND_SMS) {

            // get the phone number and sms message
            val phoneNumber = intent.extras?.getString(PHONE_NUMBER_INTENT)
            val smsMessage = intent.extras?.getString(SMS_MESSAGE_INTENT)

            if (phoneNumber == null) {
                Log.d(INTENT_TAG, EMPTY_VAL)
                return
            }
            if (smsMessage == null) {
                Log.d(INTENT_TAG, EMPTY_VAL)
                return
            }
            val smsManager = SmsManager.getDefault() as SmsManager
            // use android's SmsManager to send the sms.
            smsManager.sendTextMessage(
                phoneNumber, null,
                smsMessage, null, null
            )

            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.set_home_img)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText("sending sms to ${phoneNumber}: ${smsMessage}")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            val notificationId = createID()
            with(NotificationManagerCompat.from(context)) {
                notify(notificationId, builder.build())
            }
        }
    }

    private fun createID(): Int {
        val now = Date()
        return SimpleDateFormat("ddHHmmss", Locale.US).format(now).toInt()
    }


    private fun checkPermissions(context: Context): Boolean {
        var permission = true

        if ((ContextCompat.checkSelfPermission(
                context, Manifest.permission.RECEIVE_SMS
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.e(SMS_PERMISSON_TAG, NO_SMS_PERMISSION);
            permission = false
        }

        if ((ContextCompat.checkSelfPermission(
                context, Manifest.permission.SEND_SMS
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.e(SMS_PERMISSON_TAG, NO_SMS_PERMISSION);
            permission = false
        }

        if ((ContextCompat.checkSelfPermission(
                context, Manifest.permission.READ_SMS
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            Log.e(SMS_PERMISSON_TAG, NO_SMS_PERMISSION);
            permission = false
        }

        return permission
    }


}