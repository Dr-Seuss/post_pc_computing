package com.example.honeyimhome

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.SEND_SMS
import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.os.Build.VERSION_CODES
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.multidex.MultiDex
import androidx.work.*
import com.example.honeyimhome.LocalSendSmsBroadcastReceiver.Companion.PHONE_NUMBER_INTENT
import com.example.honeyimhome.LocalSendSmsBroadcastReceiver.Companion.SMS_MESSAGE_INTENT
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit
import kotlin.math.abs

class MsgHandler : Application() {

    var receiver = LocalSendSmsBroadcastReceiver()

    companion object {
        const val ACTION_SEND_SMS = "POST_PC.ACTION_SEND_SMS"
        const val CHANNEL_ID = "unique sms channel"
        const val APP_TAG = "honeyImHome"
    }

    @RequiresApi(VERSION_CODES.O)
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        configureReceiver()
        val work: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<SmsWorker>(15, TimeUnit.MINUTES).build()
        WorkManager.getInstance(this).enqueue(work)

    }

    private fun configureReceiver() {

        LocalBroadcastManager.getInstance(this).registerReceiver(
            receiver, IntentFilter(ACTION_SEND_SMS)
        );
    }
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(this)
    }
    @RequiresApi(VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = getString(R.string.sms_channel_id)
        val descriptionText = getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    class SmsWorker(context: Context, workerParams: WorkerParameters) :
        Worker(context, workerParams) {

        private lateinit var fusedLocationClient: FusedLocationProviderClient
        private lateinit var locationRequest: LocationRequest
        private var mContext: Context = context
        private val sharedPreferences =
            mContext.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE)!!
        var phoneNumber: String? = null
        var smsMessage: String? = null


        companion object {
            const val CUR_MSG_LAT = "CUR_MSG_LAT"
            const val CUR_MSG_LONG = "CUR_MSG_LONG"
            const val CUR_MSG_ACCUR = "CUR_MSG_ACCUR"
        }

        private val locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                if (locationResult.lastLocation.accuracy >= 50) {
                    return
                }
                val cur = LocationInfo(
                    locationResult.lastLocation.latitude,
                    locationResult.lastLocation.longitude,
                    locationResult.lastLocation.accuracy.toDouble()
                )
                onStopLocate()

                val prev = getCurrentLocationInfo()
                onSaveToSP(cur)

                if (prev == null) {
                    return
                }
                if (compareLocations(prev, cur)) {
                    return
                }
                val homeLocation = getHomeLocationInfo() ?: return
                if (compareLocations(homeLocation, cur)) {
                    sendSms()
                    return
                }
            }
        }

        override fun doWork(): Result {
            if (ActivityCompat.checkSelfPermission(
                    applicationContext,
                    ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                    applicationContext,
                    SEND_SMS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return Result.success()
            }
            fusedLocationClient =
                LocationServices.getFusedLocationProviderClient(applicationContext)
            phoneNumber =
                sharedPreferences.getString(PHONE_NUMBER_INTENT, "")
            smsMessage =
                sharedPreferences.getString(SMS_MESSAGE_INTENT, "")
            onStartLocate()
            return Result.success()
        }

        private fun compareLocations(locationA: LocationInfo, locationB: LocationInfo): Boolean {
            return ((abs(locationA.latitude!! - locationB.latitude!!) <= 50) || (abs(locationA.longitude!! - locationB.longitude!!) <= 50))
        }

        private fun sendSms() {
            Intent().also {
                val intent = Intent()
                intent.putExtra(PHONE_NUMBER_INTENT, phoneNumber)
                intent.putExtra(SMS_MESSAGE_INTENT, smsMessage)
                intent.action = MsgHandler.ACTION_SEND_SMS
                LocalBroadcastManager.getInstance(mContext).sendBroadcast(intent)
                LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(intent)
            }
        }

        private fun onSaveToSP(locationInfo: LocationInfo?) {
            val sharedPref =
                mContext.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE)

            with(sharedPref.edit()) {
                putString(CUR_MSG_LAT, "${locationInfo?.latitude}")
                putString(CUR_MSG_LONG, "${locationInfo?.longitude}")
                putString(CUR_MSG_ACCUR, "${locationInfo?.accuracy}")
                commit()
            }
        }

        private fun onStopLocate() {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }

        private fun onStartLocate() {
            locationRequest = LocationRequest.create().apply {
                interval = 10000
                fastestInterval = 5000
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            }
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }

        private fun checkPermissions(permissions: String): Boolean {
            return ActivityCompat.checkSelfPermission(
                applicationContext,
                permissions
            ) == PackageManager.PERMISSION_GRANTED
        }

        private fun getCurrentLocationInfo(): LocationInfo? {
            return getLocationInfo(
                CUR_MSG_LAT,
                CUR_MSG_LONG,
                CUR_MSG_ACCUR
            )
        }

        private fun getHomeLocationInfo(): LocationInfo? {
            return getLocationInfo(
                MainActivity.HOME_LATITUDE,
                MainActivity.HOME_LONGITUDE,
                MainActivity.HOME_ACCURACY
            )
        }

        private fun getLocationInfo(
            latitudeKey: String,
            longitude: String,
            accuracy: String
        ): LocationInfo? {
            val sharedPref = mContext.getSharedPreferences(APP_TAG, Context.MODE_PRIVATE)
            val latitudeString = sharedPref.getString(latitudeKey, null)
            val longitudeString = sharedPref.getString(longitude, null)
            val accuracyString = sharedPref.getString(accuracy, null)
            var locationInfo: LocationInfo? = null
            locationInfo = LocationInfo(
                latitudeString?.toDouble(),
                longitudeString?.toDouble(),
                accuracyString?.toDouble()
            )
            return locationInfo
        }

    }

}