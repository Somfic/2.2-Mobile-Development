package com.example.mobile_development_2_2.data

import org.osmdroid.util.GeoPoint

class GeoObject {
    var geoPoint = GeoPoint(0,0)
    var name = String

    constructor(geoPoint: GeoPoint, name: String.Companion) {
        this.geoPoint = geoPoint
        this.name = name
    }
}