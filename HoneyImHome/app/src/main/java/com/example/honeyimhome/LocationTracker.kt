package com.example.honeyimhome

class LocationTracker {
    /**
     * start tracking the location and send a "started" boradcast intent
     */
    fun startTracking() {} //TODO "startTracking()" method, add a basic check that assert you have the runtime location permission. If yes continue to track location, if not just log some error to logcat and don't do anything (optional - send an "error" broadcast and listen to it in the activity would and show some error in the UI).

    /**
     * which will stop tracking and send a "stopped" broadcast intent.
     */
    fun stopTracking() {}
}