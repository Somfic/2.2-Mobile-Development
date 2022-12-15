package com.example.mobile_development_2_2.data

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.LocationServices
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationProvider (context : Context) {
    private val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    suspend fun getLastLocation(): Result<Location> = suspendCoroutine { continuation ->
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                if (it !== null) {
                    continuation.resume(Result.success(it))
                } else {
                    continuation.resume(Result.failure(Exception("could not find location")))
                }
            }
            .addOnFailureListener {
                continuation.resume(Result.failure(it))
            }
    }

    @SuppressLint("MissingPermission")
    suspend fun getCurrentLocation(
        request: CurrentLocationRequest,
    ): Result<Location> = suspendCoroutine { continuation ->
        fusedLocationProviderClient.getCurrentLocation(request, null).addOnSuccessListener {
                if (it !== null) {
                    continuation.resume(Result.success(it))
                } else {
                    continuation.resume(Result.failure(Exception("could not find location")))
                }
            }.addOnFailureListener {
                continuation.resume(Result.failure(it))
            }
    }
}