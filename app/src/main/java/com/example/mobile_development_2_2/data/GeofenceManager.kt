package com.example.mobile_development_2_2.data

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.internal.NoOpContinuation.context

open class GeofenceManager () {

        private var geofencingClient: GeofencingClient = null
        private var geofenceHelper: GeofenceHelper = null
    companion object createSingelton : GeofenceManager (context: Context)



        fun setGeofenceLocation(lat: Double, lng: Double) {
            var geofence: Geofence? = geofenceHelper.getGeofence(lat, lng)

            var geofencingRequest: GeofencingRequest? = geofence?.let {
                geofenceHelper.geofencingRequest(
                    it
                )
            }
            var pendingIntent: PendingIntent? = geofenceHelper.getPendingIntent()
            if (geofencingRequest != null) {
                if (pendingIntent != null) {
                    geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                        .addOnSuccessListener {
                            Log.d(
                                ContentValues.TAG,
                                "Geofence added " + geofencingRequest.geofences[0].latitude + " " + geofencingRequest.geofences[0].longitude
                            )
                        }
                        .addOnFailureListener { e ->
                            Log.d(ContentValues.TAG, "onFailure: " + geofenceHelper.getErrorString(e))
                        }
                }
            }
        }

    }

}