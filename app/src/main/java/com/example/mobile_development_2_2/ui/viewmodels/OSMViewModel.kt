package com.example.mobile_development_2_2.ui.viewmodels


import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mobile_development_2_2.data.GeofenceHelper
import com.example.mobile_development_2_2.data.GetLocationProvider
import com.example.mobile_development_2_2.map.route.POI
import com.google.android.gms.location.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
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

        val avans = POI(
            name = "Avans",
            location = GeoPoint(51.5856, 4.7925),
            imgId = 1,//R.drawable.img_poi1,
            streetName = "street1",
            longDescription = "description of Avans",
            shortDescription = "short description of Avans",
            imgMap = 1,
            visited = true


        )

        // TODO: Move to POI repository
        val breda = POI(
            name = "Breda",
            location = GeoPoint(51.5719, 4.7683),
            imgId = 1,//R.drawable.img_poi2,
            streetName = "street2",
            longDescription = "description of Breda",
            shortDescription = "short description of Avans",
            imgMap = 1,
            visited = false,
        )

        // TODO: Move to POI repository
        val amsterdam = POI(
            name = "Amsterdam",
            location = GeoPoint(52.3676, 4.9041),
            imgId = 1,//R.drawable.img_poi1,
            streetName = "street3",
            longDescription = "description of Amsterdam",
            shortDescription = "short description of Avans",
            imgMap = 1,
            visited = false
        )

        // TODO: Move to POI repository
        val cities = listOf(
            avans,
            breda,
            amsterdam,
        )
        return cities

    }


    val provider = Provider(
        coroutineScope = viewModelScope,
        locationFlow = lastLocations,
    )

    fun invoke () {
        for (it in pois) {
            AddGeofence(it.location.latitude,it.location.longitude)
        }
        //AddGeofence(51.5856, 4.7925)
    }

    fun AddGeofence(lat: Double, lng: Double, ) {
        var geofence: Geofence? = geofenceHelper.getGeofence("geo", lat, lng)
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
