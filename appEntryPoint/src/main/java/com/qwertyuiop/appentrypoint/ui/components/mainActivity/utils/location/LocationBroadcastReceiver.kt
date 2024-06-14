package com.qwertyuiop.appentrypoint.ui.components.mainActivity.utils.location

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.location.LocationManager

class LocationBroadcastReceiver(
    private val locationManager: LocationManager,
    private val locationOnReceiveAction: LocationOnReceiveAction
) : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        val locationEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        locationOnReceiveAction.onReceive(locationEnabled)
    }
}