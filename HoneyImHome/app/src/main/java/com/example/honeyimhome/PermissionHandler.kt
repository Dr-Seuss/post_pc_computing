//package com.example.honeyimhome
//
//import android.content.Context
//import android.content.pm.PackageManager
//import android.util.Log
//import androidx.core.app.ActivityCompat
//import java.util.jar.Manifest
//
//interface PermissionHandler {
//
//    companion object {
//        private const val PERMISSION_TAG = "PERMISSION"
//
//        fun checkPermissions(context: Context, permissions: String): Boolean {
//
//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    permissions
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                Log.e(PERMISSION_TAG, "NO PERMISSION")
//                ActivityCompat.requestPermissions( //TODO where do we check that?
//                    activity,
//                    new String[]{ permissions},
//                    REQUEST_CODE_PERMISSION_SMS);
//                return false
//            } else {
//                return true
//
//            }
//        }
//
//
//    }
//}