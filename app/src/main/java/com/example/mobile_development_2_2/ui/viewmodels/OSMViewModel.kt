package com.example.mobile_development_2_2.ui.viewmodels


import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_development_2_2.data.GeofenceHelper
import com.example.mobile_development_2_2.map.gps.GetLocationProvider
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.map.route.RouteManager
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class OSMViewModel(getLocationProvider: GetLocationProvider, context : Context) : ViewModel() {


    private var geofencingClient: GeofencingClient = LocationServices.getGeofencingClient(context)
    private var geofenceHelper: GeofenceHelper = GeofenceHelper(context)
    var currentLocation : Location? = null
    private val lastLocations = getLocationProvider().shareIn(
        scope = viewModelScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    val pois = getLocations()


    private fun getLocations(): List<POI> {
        val route = RouteManager.getRouteManager(null).getSelectedRoute()
        return route.POIs


    }


    val provider = Provider(
        coroutineScope = viewModelScope,
        locationFlow = lastLocations,
    )

    fun invoke () {
        for (it in pois) {
            setGeofenceLocation(it.location.latitude,it.location.longitude, it.name)
        }
        //this method changes the location of the geofence,
        //keep in mind there is always 1 active geofence which should be the next geofence in the route,
        //"ïd" in this method should be the name of the geofence, this will be shown in the notification
        setGeofenceLocation(51.5856, 4.7925, "geo")
    }

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

    class Provider(
        private val coroutineScope: CoroutineScope,
        private val locationFlow: Flow<Location>,

        ): IMyLocationProvider {
        private var currentLocation: Location? = null
        private var job: Job? = null
        lateinit var locationListener: LocationListener

        override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
            if (job?.isActive == true) return true
            Log.d("Location", "start location"  )
            job = coroutineScope.launch {
                locationFlow
                    .onEach {
                        myLocationConsumer?.onLocationChanged(it, this@Provider)
                    }
                    .collect { location -> currentLocation = location

                        locationListener.onLocationChanged(currentLocation!!)
                        Log.d("Location", "${currentLocation!!.latitude} , ${currentLocation!!.longitude}"  )}

            }
            return true
        }

        override fun stopLocationProvider() {
            if (job?.isActive == true) {
                job?.cancel()
            }
        }

        override fun getLastKnownLocation(): Location? = currentLocation

        override fun destroy() = stopLocationProvider()
    }
}
