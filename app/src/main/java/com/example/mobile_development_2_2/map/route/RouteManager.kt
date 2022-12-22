package com.example.mobile_development_2_2.map.route

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.data.GeofenceHelper
import com.example.mobile_development_2_2.data.Lang
import com.example.mobile_development_2_2.gui.MainActivity
import com.example.mobile_development_2_2.map.route.Route.Companion.TestRoute

import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson

class RouteManager {
    val LOG_TAG = "RouteManager"

    var routes: List<Route>

    var context: Context?
    lateinit var selectedRoute: Route
    lateinit var currentPoi : POI

    private constructor(context: Context?) {
        Log.d(LOG_TAG, "constructor")
        this.context = context
        routes = GenerateRoutes()
        selectedRoute = routes.get(0)
        currentPoi = selectedRoute.POIs.get(0)
    }


    fun GenerateRoutes(): List<Route> {
        Log.d(LOG_TAG, "generating routes")
        val jsonString: String =
            context?.resources!!.openRawResource(R.raw.historische_kilometer).bufferedReader()
                .use { it.readText() }
        val gson = Gson()
        val routes = gson.fromJson(jsonString, Array<Route>::class.java).toList()
        for (it in routes) {
            it.started = mutableStateOf(false)
            for (poi in it.POIs) {
                poi.visited = false

                //poi.shortDescription = getStringById(poi.shortDescription)

                if (poi.imgMap == null) {
                    poi.imgMap = "image404.png"
                }
                if (poi.img == null) {
                    poi.img = "image404.png"
                }
            }
        }
        return routes
    }

    fun GetRoutes(): List<Route> {
        Log.d(LOG_TAG, "giving routes")
        return routes
    }


    fun setRouteState(started: Boolean) {
        Log.d(LOG_TAG, "setting route state")
        getRouteByName(selectedRoute.name)?.started?.value = started
    }

    fun getRouteByName(name: String): Route? {
        Log.d(LOG_TAG, "giving route by name")
        for (route in routes) {
            if (route.name == name)
                return route
        }
        return null
    }

    fun selectItem(route: Route) {
        Log.d("a", "route selected")
        selectedRoute = route
    }

    @JvmName("getSelectedItem1")
    fun getSelectedRoute(): Route {
        Log.d(LOG_TAG, "gicing selected route")
        return selectedRoute
    }

    fun get_CurrentPoi(): POI {
        Log.d(LOG_TAG, "giving current poi")
        return currentPoi
    }

    fun selectPOI(poi: POI) {
        Log.d(LOG_TAG, "selecting poi")
        Route.selectItem(poi)
    }

    fun getSelectedPOI(): POI {
        Log.d(LOG_TAG, "giving selected poi")
        return Route.getSelectedPOI()
    }

    fun getStringById(idName: String): String {
        Log.d(LOG_TAG, "getting strtring resource by name")
        return Lang.get(context?.resources!!.getIdentifier(idName, "string", context?.packageName))
    }

    fun triggeredGeofence() {

        //set visited poi true
        for (poi in selectedRoute.POIs) {
            if (!poi.visited) {
                poi.visited = true
                continue
            }
        }

        //get next poi in route
        var routeFinished: Boolean = true

        for (poi in selectedRoute.POIs) {
            if (!poi.visited) {
                currentPoi = poi
                setGeofenceLocation(poi.location.latitude, poi.location.longitude)
                routeFinished = false
                continue
            }
        }
        if(routeFinished) {
            //route finished
        }
    }

    fun setGeofenceLocation(lat: Double, lng: Double) {
        var geofence: Geofence? = geofenceHelper?.getGeofence(lat, lng)

        var geofencingRequest: GeofencingRequest? = geofence?.let {
            geofenceHelper?.geofencingRequest(
                it
            )
        }
        var pendingIntent: PendingIntent? = geofenceHelper?.getPendingIntent()
        if (geofencingRequest != null) {
            if (pendingIntent != null) {
                geofencingClient?.addGeofences(geofencingRequest, pendingIntent)
                    ?.addOnSuccessListener {
                        Log.d(
                            ContentValues.TAG,
                            "Geofence added " + geofencingRequest.geofences[0].latitude + " " + geofencingRequest.geofences[0].longitude
                        )
                    }
                    ?.addOnFailureListener { e ->
                        Log.d(
                            ContentValues.TAG,
                            "onFailure: " + geofenceHelper?.getErrorString(e)
                        )
                    }
            }
        }
    }


    companion object {
        private var routeManager: RouteManager? = null
        private var geofencingClient: GeofencingClient? = null
        private var geofenceHelper: GeofenceHelper? = null

        private fun TestRoutes(): List<Route> {
            Log.d("RouteManager", "testroutes")
            var testRoute1 = Route.TestRoute("testRoute1")
            var testRoute2 = Route.TestRoute2("testRoute2")
            //var testRoute3 = Route.TestRoute("testRoute3")

            var routes = listOf<Route>(
                testRoute1,
                testRoute2,
                //testRoute3
            )

            return routes
        }

        fun getRouteManager(context: Context?): RouteManager {
            Log.d("RouteManager", "getroutemanager")

            if (routeManager == null) {
                Log.d("RouteManager", "making routemanager")
                routeManager = RouteManager(context)
                geofenceHelper = GeofenceHelper(context)
                geofencingClient = context?.let { LocationServices.getGeofencingClient(it) }
            }
            return routeManager as RouteManager
        }


    }
}