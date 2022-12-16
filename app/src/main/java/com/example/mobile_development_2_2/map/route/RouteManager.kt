package com.example.mobile_development_2_2.map.route

class RouteManager {


    companion object{
        fun TestRoutes(): List<Route>{
            var testRoute1 = Route.TestRoute("testRoute1")
            var testRoute2 = Route.TestRoute("testRoute2")
            var testRoute3 = Route.TestRoute("testRoute3")

            var routes = listOf<Route>(
                testRoute1,
                testRoute2,
                testRoute3
            )

            return routes
        }
    }
}