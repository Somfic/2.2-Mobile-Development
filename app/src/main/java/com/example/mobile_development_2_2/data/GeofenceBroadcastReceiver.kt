package com.example.mobile_development_2_2.data

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.gui.MainActivity
import com.example.mobile_development_2_2.gui.fragments.MapFragment
import com.example.mobile_development_2_2.map.route.RouteManager
import com.google.android.gms.location.*

class GeofenceBroadcastReceiver : BroadcastReceiver() {

    private lateinit var context: Context;

    private var geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)
    private var geofenceHelper: GeofenceHelper = GeofenceHelper(context)

    fun setGeofenceLocation(lat: Double, lng: Double, id : String  ) {
        geofenceHelper.getPendingIntent()?.let { geofencingClient.removeGeofences(it) }
        var geofence: Geofence? = geofenceHelper.getGeofence(id, lat, lng)

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


    private val TAG = "GeofenceBroadcastReceiver"
    // ...
    override fun onReceive(context: Context, intent: Intent) {
        this.context = context

        var notificationHelper = NotificationHelper(context)


        //Toast.makeText(context, "geofence triggered", Toast.LENGTH_SHORT).show()
        Log.d(TAG, "onReceive: geofence triggered")

        var geofencingEvent : GeofencingEvent? = GeofencingEvent.fromIntent(intent)

        if (geofencingEvent != null) {
            if(geofencingEvent.hasError()) {
                Log.d(TAG, "onReceive: Error geofencing event")
            }
        }

        var geofenceList : List<Geofence>? = geofencingEvent?.triggeringGeofences

        if (geofenceList != null) {
            for (geofence in geofenceList) {
                Log.d(TAG, "onReceive: " + geofence.requestId + " triggered ")
                notificationHelper.sendHighPriorityNotification("Geofence triggered",geofence.requestId, MainActivity::class.java)

                // find next POI
                val poi : POI? = RouteManager.getNextPOI()
                if(poi != null) {
                    setGeofenceLocation(poi.location.latitude, poi.location.longitude, poi.name)
                }

                //RouteManager.SelectPOI()
                PopupHelper.SetState(true)
            }
        }
    }
}
