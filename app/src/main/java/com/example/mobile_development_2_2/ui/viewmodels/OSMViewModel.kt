package com.example.mobile_development_2_2.ui.viewmodels

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel


import com.example.mobile_development_2_2.data.LocationUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.IMyLocationConsumer
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider

class OSMViewModel(trackGPSLocation: LocationUseCase) : ViewModel() {

    private var job: Job? = null
    var currentLocation : Location? = null
    private val locations = trackGPSLocation().shareIn(
        scope = viewModelScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )

    // TODO: merge into single uiState

    val provider: IMyLocationProvider = Provider(
        scope = viewModelScope,
        locations = locations,
    )

    internal class Provider(
        private val scope: CoroutineScope,
        private val locations: Flow<Location>,
    ): IMyLocationProvider {
        private var lastKnownLocation: Location? = null
        private var job: Job? = null

        override fun startLocationProvider(myLocationConsumer: IMyLocationConsumer?): Boolean {
            if (job?.isActive == true) return true

            job = scope.launch {
                locations
                    .onEach {
                        myLocationConsumer?.onLocationChanged(it, this@Provider)
                    }
                    .collect { location -> lastKnownLocation = location }
            }
            return true
        }

        override fun stopLocationProvider() {
            if (job?.isActive == true) {
                job?.cancel()
            }
        }

        override fun getLastKnownLocation(): Location? = lastKnownLocation

        override fun destroy() = stopLocationProvider()
    }

    fun start() {
        if (job?.isActive == true){ currentLocation = null; return}
        Log.d("location", "start")
        job = viewModelScope.launch {

                Log.d("location", currentLocation.toString() + "ASD")
                if (locations.replayCache.isNotEmpty()) {
                    locations.collect {

                        currentLocation = locations.last()
                        Log.d("location", currentLocation.toString())
                    }
                }

        }


    }
}
