package com.example.mobile_development_2_2.data

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import com.example.mobile_development_2_2.MainActivity
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.LocationServices

class Geofencing(base: Context?) : ContextWrapper(base) {

    lateinit var geofencingClient: GeofencingClient

    fun onCreate(savedInstanceState: Bundle?, activity: MainActivity) {
        // ..

        geofencingClient = LocationServices.getGeofencingClient(activity)

    }

    fun create(latitude : Double, longitude : Double, radius : Float) {

        Geofence.Builder()
            .setRequestId("test")
            .setCircularRegion(
                latitude,
                longitude,
                radius,
            )
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .build();
    }

}