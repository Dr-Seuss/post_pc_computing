package com.example.honeyimhome

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class LocationTracker(var context: Context) {
    private var locationListener: LocationListener? = null
    private var locationManager: LocationManager

    companion object {
        var isTracking = false
    }

    init {
        locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                val intent = Intent()
                intent.action = "new_location"
                intent.putExtra("latitude", location.latitude)
                intent.putExtra("longitude", location.longitude)
                intent.putExtra("accuracy", location.accuracy)
                context.sendBroadcast(intent)
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                TODO("Not yet implemented")
            }

            override fun onProviderEnabled(provider: String?) {
                TODO("Not yet implemented")
            }

            override fun onProviderDisabled(provider: String?) {
                TODO("Not yet implemented")
            }
        }

        locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }

    @SuppressLint("MissingPermission")
    fun startTracking() {
        isTracking = true

        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                context as Activity, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                MainActivity.REQUEST_LOCATION_CODE
            )
        }
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            1000,
            0.1f, locationListener
        )

        val intent = Intent()
        val lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        if (lastLocation != null) {
            intent.putExtra("latitude", lastLocation.latitude)
            intent.putExtra("longitude", lastLocation.longitude)
            intent.putExtra("accuracy", lastLocation.accuracy)
        }
        intent.action = "started"
        context.sendBroadcast(intent)
    }

    fun stopTracking() {
        isTracking = false
        locationManager.removeUpdates(this.locationListener)

        val intent = Intent()
        intent.action = "stopped"
        context.sendBroadcast(intent)
    }
}
//TODO "startTracking()" method, add a basic check that assert you have the runtime location permission.
// If yes continue to track location, if not just log some error to logcat and don't do anything
// (optional - send an "error" broadcast and listen to it in the activity would and show some error in the UI).


