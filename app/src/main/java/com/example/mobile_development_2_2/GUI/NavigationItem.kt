package com.example.mobile_development_2_2.GUI

import com.example.mobile_development_2_2.R

sealed class NavigationItem(var route: String, var icon: Int, var title: String){
    object Home : NavigationItem("home", R.drawable.ic_home, "Home")
    object Map : NavigationItem("map", R.drawable.ic_map, "Map")
    object POIs : NavigationItem("POIs", R.drawable.ic_poi, "POIs")
}
