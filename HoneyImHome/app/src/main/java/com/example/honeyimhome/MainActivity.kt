package com.example.honeyimhome

import android.Manifest
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.util.Log
import android.view.View.*
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.honeyimhome.LocalSendSmsBroadcastReceiver.Companion.PHONE_NUMBER_INTENT
import com.example.honeyimhome.LocalSendSmsBroadcastReceiver.Companion.SMS_MESSAGE_INTENT
import com.example.honeyimhome.LocationTracker.Companion.isTracking
import com.example.honeyimhome.MsgHandler.Companion.ACTION_SEND_SMS
import com.example.honeyimhome.MsgHandler.Companion.APP_TAG

class MainActivity : AppCompatActivity() {
    private lateinit var locationTracker: LocationTracker
    private var curHomeLocation: LocationInfo? = null
    private var currentLocation: LocationInfo? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val smsMessage = "Honey im Home!"
    private var phoneNumber = ""

    companion object {
        const val HOME_LOCATION_MESSAGE = "Current Home Location is: "
        private const val LOCATION_LATITUDE = "LOCATION_LATITUDE"
        private const val LOCATION_LONGITUDE = "LOCATION_LONGITUDE"
        private const val LOCATION_ACCURACY = "LOCATION_ACCURACY"
        var REQUEST_LOCATION_CODE = 34
        var REQUEST_SMS_CODE = 32 //why 154x in TA?
        const val HOME_LATITUDE = "homeLatitude"
        const val HOME_LONGITUDE = "homeLongitude"
        const val HOME_ACCURACY = "homeAccuracy"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationTracker = LocationTracker(baseContext)
        sharedPreferences = getSharedPreferences(APP_TAG, Context.MODE_PRIVATE)
        phoneNumber = sharedPreferences.getString(PHONE_NUMBER_INTENT, "").toString()
        onClickSetSms()
        onClickSendSms()
        onClickStartTracking()
        onClickStopTracking()
        onClickSetHomeLocation()
        onClickResetHomeLocation()
        configureReceiver()
        onReturnToApp()
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    private fun onClickStartTracking() {
        findViewById<Button>(R.id.start_tracking_location).setOnClickListener {
            checkLocationPermission()
            when {
                (!isTracking) && ((ContextCompat.checkSelfPermission(
                    baseContext,
                    ACCESS_FINE_LOCATION
                ) == PERMISSION_GRANTED))
                -> {
                    locationTracker.startTracking()
                    findViewById<Button>(R.id.start_tracking_location).visibility = GONE
                    findViewById<Button>(R.id.stop_tracking_location).visibility = VISIBLE
                }
            }
        }
    }

    private fun onClickStopTracking() {
        findViewById<Button>(R.id.stop_tracking_location).setOnClickListener {
            if (isTracking) {
                locationTracker.stopTracking()
                isTracking = false
                findViewById<Button>(R.id.stop_tracking_location).visibility = GONE
                findViewById<Button>(R.id.start_tracking_location).visibility = VISIBLE
            }
        }
    }

    private fun clearLocationData() { // delete all the location data screen and SP
        findViewById<TextView>(R.id.location_longitude).text = ""
        findViewById<TextView>(R.id.location_latitude).text = ""
        findViewById<TextView>(R.id.tracking_accuracy).text = ""
        onDeleteSP()
    }

    private fun configureReceiver() {
        val filter = IntentFilter()
        filter.addAction("started")
        filter.addAction("stopped")
        filter.addAction("new_location")
        registerReceiver(receiver, filter)
    }

    @SuppressLint("SetTextI18n")
    private fun onClickSetHomeLocation() {
        findViewById<Button>(R.id.set_home_location).setOnClickListener {
            curHomeLocation = LocationInfo(
                this.currentLocation!!.latitude,
                this.currentLocation!!.longitude,
                this.currentLocation!!.accuracy
            )
            findViewById<Button>(R.id.reset_home_location).visibility = VISIBLE
            findViewById<Button>(R.id.stop_tracking_location).visibility = VISIBLE
            findViewById<TextView>(R.id.current_home_location).text =
                "$HOME_LOCATION_MESSAGE${this.curHomeLocation?.longitude},${curHomeLocation?.latitude},${curHomeLocation?.accuracy} "
            with(sharedPreferences.edit()) {

                putString(
                    HOME_LATITUDE,
                    "${curHomeLocation?.latitude}"
                )

                putString(
                    HOME_LONGITUDE,
                    "${curHomeLocation?.longitude}"
                )

                putString(
                    HOME_ACCURACY,
                    "${curHomeLocation?.accuracy}"
                )

                apply()
            }
        }
    }

    private fun onReturnToApp() {
        if (isTracking && ContextCompat.checkSelfPermission(
                this, ACCESS_FINE_LOCATION
            ) == PERMISSION_GRANTED
        ) {
            currentLocation = onLoadFromSP()
            findViewById<Button>(R.id.stop_tracking_location).visibility = VISIBLE

        }
    }

    @SuppressLint("SetTextI18n")
    private fun onClickResetHomeLocation() {
        findViewById<Button>(R.id.reset_home_location).setOnClickListener { // clear text
            findViewById<TextView>(R.id.current_home_location).text = "no current home location"
            curHomeLocation = null
            clearLocationData()
            findViewById<Button>(R.id.reset_home_location).visibility = GONE
            findViewById<Button>(R.id.start_tracking_location).visibility = VISIBLE
        }
    }

    private fun setVisibility() {
        if (currentLocation?.accuracy!! < 50) {
            findViewById<Button>(R.id.set_home_location).visibility = VISIBLE
        } else {
            findViewById<Button>(R.id.set_home_location).visibility = GONE
        }
    }

    private fun checkLocationPermission() { // access problem from outside
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) != PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(ACCESS_FINE_LOCATION), REQUEST_LOCATION_CODE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var permissionGranted = true
        for (result in grantResults) {
            if (result != 0) {
                permissionGranted = false
            }
        }
        if (requestCode == REQUEST_LOCATION_CODE) {
            checkLocationPermission()
            findViewById<Button>(R.id.stop_tracking_location).visibility = VISIBLE
        } else if (requestCode == REQUEST_SMS_CODE) {
            if (!permissionGranted) {
                AlertDialog.Builder(this)
                    .setTitle("SMS Permission Needed")
                    .setMessage("This app needs SMS read&write permission to work correctly ")
                    .setPositiveButton("OK") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.RECEIVE_SMS
                            ),
                            32
                        )
                    }
                    .create()
                    .show()
            }
        }

    }

    private val receiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                when (intent.action) {
                    "new_location" -> {
                        val latitude = intent.extras!!.getDouble("latitude")
                        val longitude = intent.extras!!.getDouble("longitude")
                        val accuracy = intent.extras!!.getDouble("accuracy")
                        currentLocation = LocationInfo(latitude, longitude, accuracy)
                        findViewById<TextView>(R.id.location_latitude).text =
                            "${currentLocation!!.latitude}"
                        findViewById<TextView>(R.id.location_longitude).text =
                            "${currentLocation!!.longitude}"
                        findViewById<TextView>(R.id.tracking_accuracy).text =
                            "${currentLocation!!.accuracy}"
                        onSaveToSP()
                        setVisibility()
                    }
                    "started" -> {
                        onSaveToSP()
                        findViewById<Button>(R.id.start_tracking_location).visibility = INVISIBLE
                        findViewById<Button>(R.id.stop_tracking_location).visibility = VISIBLE
                    }
                    "stopped" -> {
                        findViewById<Button>(R.id.start_tracking_location).visibility = VISIBLE
                        findViewById<Button>(R.id.stop_tracking_location).visibility = GONE
                        findViewById<Button>(R.id.set_home_location).visibility = GONE
                        clearLocationData()
                    }
                }
            }
        }
    }

    fun onSaveToSP() {
        with(sharedPreferences.edit()) {
            putString(LOCATION_LATITUDE, "${currentLocation?.latitude}")
            putString(LOCATION_LONGITUDE, "${currentLocation?.longitude}")
            putString(LOCATION_ACCURACY, "${currentLocation?.accuracy}")
            putString(SMS_MESSAGE_INTENT, smsMessage)
            putString(PHONE_NUMBER_INTENT, phoneNumber)
            commit()
        }
    }

    private fun onLoadFromSP(): LocationInfo {
        return LocationInfo(
            sharedPreferences.getString(LOCATION_LATITUDE, null)?.toDouble(),
            sharedPreferences.getString(LOCATION_LONGITUDE, null)?.toDouble(),
            sharedPreferences.getString(LOCATION_ACCURACY, null)?.toDouble()
        )
    }

    private fun onDeleteSP() {
        sharedPreferences.edit().remove(LOCATION_LATITUDE).apply()
        sharedPreferences.edit().remove(LOCATION_LONGITUDE).apply()
        sharedPreferences.edit().remove(LOCATION_ACCURACY).apply()
        sharedPreferences.edit().remove(SMS_MESSAGE_INTENT).apply()
        sharedPreferences.edit().remove(PHONE_NUMBER_INTENT).apply()
    }

    private fun onSetPhoneNumber() {
        val editText = EditText(this)
        editText.hint = "Enter phone number:"
        editText.setSingleLine()
        val layout = FrameLayout(this)
        layout.addView(editText)
        AlertDialog
            .Builder(this)
            .setTitle("Set Phone Number")
            .setView(layout)
            .setPositiveButton(
                "OK"
            ) { dialog, which ->
                run {
                    findViewById<Button>(R.id.set_sms).visibility = VISIBLE
                    findViewById<Button>(R.id.send_sms).visibility = VISIBLE
                    phoneNumber = editText.text.toString()
                    onSaveToSP()
                }
            }.show()
    }

    private fun onClickSetSms() {
        findViewById<Button>(R.id.set_sms).setOnClickListener {
            var permissions = true

            if ((ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)
                        != PERMISSION_GRANTED
                        ) || (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS)
                        != PERMISSION_GRANTED
                        ) || ContextCompat.checkSelfPermission(
                    this, Manifest.permission.RECEIVE_SMS
                ) != PERMISSION_GRANTED
            ) {
                permissions = false
            }
            if (permissions) {
                onSetPhoneNumber()

            } else {
                AlertDialog.Builder(this)
                    .setTitle("SMS Permission Needed")
                    .setMessage("This app needs SMS read&write permission to work correctly ")
                    .setPositiveButton("OK") { dialog, which ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(
                                Manifest.permission.SEND_SMS,
                                Manifest.permission.READ_SMS,
                                Manifest.permission.RECEIVE_SMS
                            ),
                            REQUEST_SMS_CODE
                        )
                    }
                    .create()
                    .show()
            }
        }
    }

    private fun onClickSendSms() {
        findViewById<Button>(R.id.send_sms).setOnClickListener {
            val intent = Intent()
            intent.putExtra(PHONE_NUMBER_INTENT, phoneNumber)
            intent.putExtra(SMS_MESSAGE_INTENT, smsMessage)
            intent.action = ACTION_SEND_SMS
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
            Log.d("BRAHAAA", intent.toString())
        }
    }


/*
object PreferenceHelper {

private const val LOCATION_LATITUDE = "LOCATION_LATITUDE"
private const val LOCATION_LONGITUDE = "LOCATION_LONGITUDE"
private const val LOCATION_ACCURACY = "LOCATION_ACCURACY"

fun defaultPreference(context: Context): SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)

fun customPreference(context: Context, name: String): SharedPreferences = context.getSharedPreferences(name, Context.MODE_PRIVATE)

inline fun SharedPreferences.editMe(operation: (SharedPreferences.Editor) -> Unit) {
val editMe = edit()
operation(editMe)
editMe.apply()
}

var SharedPreferences.location_latitude
get() = getString(LOCATION_LATITUDE, "${homeLocation.get()?.latitude}")
set(value) {
editMe {
it.putString(LOCATION_LATITUDE, value)
}
}

var SharedPreferences.location_longitude
get() = getString(LOCATION_LONGITUDE, "")
set(value) {
editMe {
it.putString(LOCATION_LONGITUDE, value)
}
}

var SharedPreferences.location_accuracy
get() = getString(LOCATION_ACCURACY, "")
set(value) {
editMe {
it.putString(LOCATION_ACCURACY, value)
}
}
}
*/


}
