package com.example.mobile_development_2_2.ui.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel


import androidx.lifecycle.viewModelScope
import com.example.mobile_development_2_2.data.GetLocationProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class OSMViewModel(getLocationProvider: GetLocationProvider) : ViewModel() {

    var currentLocation : Location? = null
    private val locations = getLocationProvider().shareIn(
        scope = viewModelScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )


    val provider: IMyLocationProvider = Provider(
        coroutineScope = viewModelScope,
        locationFlow = locations,
    )

    internal class Provider(
        private val coroutineScope: CoroutineScope,
        private val locationFlow: Flow<Location>,
    ): IMyLocationProvider {
        private var currentLocation: Location? = null
        private var job: Job? = null

        override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
            if (job?.isActive == true) return true
            Log.d("Location", "start location"  )
            job = coroutineScope.launch {
                locationFlow
                    .onEach {
                        myLocationConsumer?.onLocationChanged(it, this@Provider)
                    }
                    .collect { location -> currentLocation = location
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
