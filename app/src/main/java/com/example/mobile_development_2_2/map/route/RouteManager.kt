package com.example.mobile_development_2_2.map.route

import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.data.GeofenceHelper
import com.example.mobile_development_2_2.data.Lang
import com.example.mobile_development_2_2.gui.fragments.home.HelpItem
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson

class RouteManager {
    val LOG_TAG = "RouteManager"

    var routes : List<Route>
    var context: Context?
    lateinit var selectedRoute: Route

    private constructor(context: Context?){
        Log.d(LOG_TAG, "constructor")
        this.context = context
        routes = GenerateRoutes()
        selectedRoute = routes.get(0)
    }



    fun GenerateRoutes(): List<Route>{
        Log.d(LOG_TAG, "generating routes")
        val jsonString: String = context?.resources!!.openRawResource(R.raw.historische_kilometer).bufferedReader().use { it.readText() }
        val gson = Gson()
        val routes =  gson.fromJson(jsonString, Array<Route>::class.java).toList()
        for (it in routes) {
            it.started = mutableStateOf(false)
            for(poi in it.POIs){
                poi.visited = false

                //poi.shortDescription = getStringById(poi.shortDescription)

                if (poi.imgMap == null){
                    poi.imgMap = "image404.png"
                }
                if (poi.img == null){
                    poi.img = "image404.png"
                }
            }
        }
        return routes
    }

    fun GetRoutes(): List<Route>{
        Log.d(LOG_TAG, "giving routes")
        return routes
    }



    fun setRouteState(started: Boolean){
        Log.d(LOG_TAG, "setting route state")
        getRouteByName(selectedRoute.name)?.started?.value = started
    }

    fun getRouteByName(name : String) : Route?{
        Log.d(LOG_TAG, "giving route by name")
        for (route in routes){
            if(route.name == name)
                return route
        }
        return null
    }

    fun selectItem(route: Route){
        Log.d("a", "route selected")
        selectedRoute = route
    }

    @JvmName("getSelectedItem1")
    fun getSelectedRoute(): Route {
        Log.d(LOG_TAG, "gicing selected route")
        return selectedRoute
    }

    fun selectPOI(poi:POI){
        Log.d(LOG_TAG, "selecting poi")
        Route.selectItem(poi)
    }

    fun getSelectedPOI(): POI{
        Log.d(LOG_TAG, "giving selected poi")
        return Route.getSelectedPOI()
    }

    fun getStringById(idName: String): String{
        Log.d(LOG_TAG, "getting strtring resource by name")
        return Lang.get(context?.resources!!.getIdentifier(idName, "string", context?.packageName))
    }





    companion object{
        private var routeManager : RouteManager? = null

         private fun TestRoutes(): List<Route>{
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

        fun getRouteManager(context: Context?): RouteManager{
            Log.d("RouteManager", "getroutemanager")

            if(routeManager == null){
                Log.d("RouteManager", "making routemanager")
                routeManager = RouteManager(context)
            }


            return routeManager as RouteManager
        }


    }
}