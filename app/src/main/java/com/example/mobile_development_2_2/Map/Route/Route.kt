package com.example.mobile_development_2_2.map.route

import com.example.mobile_development_2_2.map.route.POI

class Route(
    val name : String,
    val description : String,
    val POIs: List<POI>
    ) {

    fun addPOI(poi: POI){
        POIs.plus(poi)
    }



}
