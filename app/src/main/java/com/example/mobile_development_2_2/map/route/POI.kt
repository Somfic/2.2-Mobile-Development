package com.example.mobile_development_2_2.map.route

import org.osmdroid.util.GeoPoint

class POI (name: String, imgId: Int, location: GeoPoint, streetName: String, shortDescription: String, longDescription: String, imgMap: Int) {
    val name = name
    val location = location
    val imgId = imgId
    val streetName = streetName
    val shortDescription = shortDescription
    val longDescription = longDescription
    val imgMap = imgMap
}
