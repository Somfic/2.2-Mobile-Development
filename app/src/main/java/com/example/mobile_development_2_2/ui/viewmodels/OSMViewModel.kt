package com.example.mobile_development_2_2.ui.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel

import com.example.mobile_development_2_2.data.GeoLocation
import com.example.mobile_development_2_2.data.LocationUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.shareIn
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch

class OSMViewModel(trackGPSLocation: LocationUseCase) : ViewModel() {
    var locations = trackGPSLocation().shareIn(
        scope = viewModelScope,
        replay = 1,
        started = SharingStarted.WhileSubscribed()
    )
    private var job: Job? = null
    var currentLocation = GeoLocation(null)

    fun start() {
        if (job?.isActive == true) return
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
