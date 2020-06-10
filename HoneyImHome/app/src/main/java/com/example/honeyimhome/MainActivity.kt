package com.example.honeyimhome

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.*
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Bundle
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.honeyimhome.LocationTracker.Companion.isTracking

class MainActivity : AppCompatActivity() {
    private lateinit var locationTracker: LocationTracker
    private var curHomeLocation: LocationInfo? = null
    private var currentLocation: LocationInfo? = null
    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val HOME_LOCATION_MESSAGE = "Current Home Location is: "
        private const val LOCATION_LATITUDE = "LOCATION_LATITUDE"
        private const val LOCATION_LONGITUDE = "LOCATION_LONGITUDE"
        private const val LOCATION_ACCURACY = "LOCATION_ACCURACY"
         var REQUEST_LOCATION_CODE = 34
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationTracker = LocationTracker(baseContext)
        sharedPreferences = getSharedPreferences("HONEY_IM_HOME", Context.MODE_PRIVATE)

        onClickStartTracking()
        onClickStopTracking()
        onClickSetHomeLocation()
        onClickResetHomeLocation()
        configureReceiver()
        onReturnToApp()
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
            findViewById<TextView>(R.id.current_home_location).text =
                "$HOME_LOCATION_MESSAGE ${this.curHomeLocation?.longitude},${curHomeLocation?.latitude}"
            onSaveToSP()
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

    public fun checkLocationPermission() { // access problem from outside
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
        if (requestCode == REQUEST_LOCATION_CODE) {
            findViewById<Button>(R.id.stop_tracking_location).visibility = VISIBLE
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
                        findViewById<Button>(R.id.start_tracking_location).visibility = GONE
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

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(receiver)
    }

    fun onSaveToSP() {
        with(sharedPreferences.edit()) {
            putString(LOCATION_LATITUDE, "${curHomeLocation?.latitude}")
            putString(LOCATION_LONGITUDE, "${curHomeLocation?.longitude}")
            putString(LOCATION_ACCURACY, "${curHomeLocation?.accuracy}")
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