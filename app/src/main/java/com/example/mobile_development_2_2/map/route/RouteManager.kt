package com.example.mobile_development_2_2.map.route

import android.content.res.Resources
import android.util.Log
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.gui.fragments.home.HelpItem
import com.google.gson.Gson

class RouteManager {


    companion object{
        fun TestRoutes(): List<Route>{
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
        fun GetRoutes(resources: Resources): List<Route>{
            val jsonString: String = resources.openRawResource(R.raw.historische_kilometer).bufferedReader().use { it.readText() }
            val gson = Gson()
            val routes =  gson.fromJson(jsonString, Array<Route>::class.java).toList()
            for (it in routes) {
                for(poi in it.POIs){
                    poi.visited = false
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
        var selectedItem = TestRoutes().get(0)



        fun selectItem(route: Route){
            Log.d("a", "Item selected")
            selectedItem = route
        }

        @JvmName("getSelectedItem1")
        fun getSelectedRoute(): Route {
            return selectedItem
        }

        fun selectPOI(poi:POI){
            Route.selectItem(poi)
        }

        fun getSelectedPOI(): POI{
            return Route.getSelectedPOI()
        }
    }
}