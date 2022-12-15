package com.example.mobile_development_2_2.data

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingRequest



class GeofenceHelper(base: Context?) : ContextWrapper(base) {



    private val TAG = "GeofenceHelper"
    private lateinit var pendingIntent: PendingIntent

    fun geofencingRequest  (geofence : Geofence) : GeofencingRequest? {
        return GeofencingRequest.Builder()
            .addGeofence(geofence)
            .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            .build()
    }

    fun getGeofence(id : String, lat : Double, lng : Double) : Geofence? {
        return Geofence.Builder()
            .setRequestId(id)
            .setCircularRegion(lat, lng, 20f)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER)
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .build()

    }

    fun getPendingIntent() : PendingIntent? {
        if(pendingIntent != null) {
            return pendingIntent
        }
        val intent = Intent(this, GeofenceBroadcastReceiver::class.java)
        pendingIntent = PendingIntent.getBroadcast(this, 6969, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        return pendingIntent
    }
}