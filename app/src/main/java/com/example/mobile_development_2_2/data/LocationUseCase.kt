package com.example.mobile_development_2_2.data

import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map

class LocationUseCase(
    private val locationRepository: LocationProvider,
) {
    operator fun invoke() = locationRepository.locationTracker()
        .map { it.getOrNull() }
        .filterNotNull()
}