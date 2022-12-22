package com.example.mobile_development_2_2.map.route

import org.osmdroid.util.GeoPoint

data class POI (
    val name: String,
    val location: GeoPoint,
    var img : String,
    val streetName: String,
    val shortDescription: String,
    val longDescription: String,
    var imgMap : String = "ic_map.png",
    var visited : Boolean =false
)

