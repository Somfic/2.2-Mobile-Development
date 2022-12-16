package com.example.mobile_development_2_2.data

import android.content.Context
import android.location.Location
import com.google.android.gms.location.*

import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import org.osmdroid.util.GeoPoint

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationProvider (context : Context,
                        ) {
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    fun Location.toGeoLocation() = GeoLocation(
        geoPoint = GeoPoint(this.longitude,this.latitude)
    )

    suspend fun lastLocation(): Result<GeoLocation> = last().map { it.toGeoLocation() }
    fun locationTracker(): Flow<Result<GeoLocation>> = track().map { result -> result.map { it.toGeoLocation() } }

    private suspend fun last(): Result<Location> = suspendCoroutine { continuation ->
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                // Note: it is of type Location! a platform type that can be null
                if (it !== null) {
                    continuation.resume(Result.success(it))
                } else {
                    continuation.resume(Result.failure(Exception("No location available")))
                }
            }
            .addOnFailureListener {
                continuation.resume(Result.failure(it))
            }
    }

    private fun track(
    ): Flow<Result<Location>> = callbackFlow {
        val locationRequest = LocationRequest.Builder(500)
            .setGranularity(Granularity.GRANULARITY_FINE)
            .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
            .setWaitForAccurateLocation(true)
            .setMaxUpdateAgeMillis(LocationRequest.Builder.IMPLICIT_MAX_UPDATE_AGE)
            .setMinUpdateIntervalMillis(LocationRequest.Builder.IMPLICIT_MIN_UPDATE_INTERVAL)
            .build()
        val locationListener = LocationListener { location -> trySend(Result.success(location)) }

        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationListener, null)

        awaitClose {
            fusedLocationProviderClient.removeLocationUpdates(locationListener)
        }
    }.catch {
        emit(Result.failure(it))
    }
}