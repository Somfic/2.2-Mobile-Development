package com.example.mobile_development_2_2.gui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.location.Location

import android.location.LocationManager
import android.provider.ContactsContract.CommonDataKinds.Website
import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.lifecycle.viewModelScope

import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.data.GeoLocation
import com.example.mobile_development_2_2.data.LocationProvider
import com.example.mobile_development_2_2.data.LocationUseCase
import com.example.mobile_development_2_2.map.gps.GPSLocationProvider
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.ui.viewmodels.OSMViewModel
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.Granularity
import com.google.android.gms.location.Priority
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.IconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline


class MapFragment {


    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun MapScreen(viewModel: OSMViewModel, modifier: Modifier) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            val premissions = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )

            if (premissions.allPermissionsGranted) {
                viewModel.start()

            }
            OSM(
                modifier = modifier,
                locations = getLocations(),
                routePoints = getLocations().map { it.location }.toMutableList(),
                currentLocation = viewModel.currentLocation
            )
            if (!premissions.allPermissionsGranted) {
                Column() {

                    Text(text = "No Location Premission granted", color = Color.Red)
                }
            }
            Row() {


                Text(
                    text = "© OpenStreetMap contributors",
                    fontSize = 8.sp,
                    modifier = Modifier
                        .background(Color.White, RectangleShape)
                        .align(Alignment.Bottom)
                )


            }
        }
    }
}

//TODO DELETE And make a list provider
private fun getLocations(): List<POI> {

    val avans = POI(
        name = "Avans",
        location = GeoPoint(51.5856, 4.7925),
        imgId = 1,//R.drawable.img_poi1,
        streetName = "street1",
        description = "description of Avans"
    )

    // TODO: Move to POI repository
    val breda = POI(
        name = "Breda",
        location = GeoPoint(51.5719, 4.7683),
        imgId = 1,//R.drawable.img_poi2,
        streetName = "street2",
        description = "description of Breda"
    )

    // TODO: Move to POI repository
    val amsterdam = POI(
        name = "Amsterdam",
        location = GeoPoint(52.3676, 4.9041),
        imgId = 1,//R.drawable.img_poi1,
        streetName = "street3",
        description = "description of Amsterdam"
    )

    // TODO: Move to POI repository
    val cities = listOf(
        avans,
        breda,
        amsterdam,
    )
    return cities

}


@Composable
private fun OSM(

    modifier: Modifier = Modifier,
    locations: List<POI> = listOf(),
    routePoints: MutableList<GeoPoint> = mutableListOf(),
    currentLocation: GeoLocation? = null,
) {


    val context = LocalContext.current
    val locationProvider = GPSLocationProvider(context)


    val mapView = remember {
        MapView(context)
    }
    //FIXME poilayer kan toegevoegd worden als er point of interest zijn
    val poiOverlay = remember {
        val listener = object : ItemizedIconOverlay.OnItemGestureListener<POIItem> {
            override fun onItemSingleTapUp(index: Int, item: POIItem?): Boolean {
                println("onItemSingleTapUp")
                return true
            }

            override fun onItemLongPress(index: Int, item: POIItem?): Boolean {
                println("onItemLongPress")
                return false
            }
        }

        ItemizedIconOverlay(
            mutableListOf<POIItem>(),
            ContextCompat.getDrawable(
                context,
                org.osmdroid.library.R.drawable.ic_menu_mylocation
            ),
            listener,
            context
        )
    }


    val currentRoute = remember {
        Polyline()
    }
    currentRoute.outlinePaint.color = ContextCompat.getColor(context, R.color.purple_200)
    ContextCompat.getColor(context, R.color.purple_200).dec()
    currentRoute.setPoints(routePoints)
    //todo Als we willen dat de gelopen route wordt getekend en/of een correctieroute wordt getekend
//        val walkedRoute = remember {
//            Polyline()
//        }
//        walkedRoute.color = R.color.black
//        val correctionRoute = remember {
//            Polyline()
//        }
//        correctionRoute.color = R.color.teal_700


    val myLocation = remember(mapView) {

        //todo make follow current location

        // location: Location = Location(LocationProvider(context).)

            val thisLocation = currentLocation?:GeoLocation(null)

            IconOverlay(
                thisLocation.geoPoint,
                ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.person)
            )

    }




    AndroidView(
        factory = {
            mapView.apply {
                setTileSource(TileSourceFactory.MAPNIK)
                isTilesScaledToDpi = true
                controller.setCenter(GeoPoint(51.5856, 4.7925)) // Avans
                controller.setZoom(17.0)

                //mapView.overlays.add(poiOverlay)


                mapView.overlays.add(poiOverlay)
                if (currentLocation != null) {
                    mapView.overlays.add(myLocation)
                }
                mapView.overlays.add(currentRoute)
            }
        },
        modifier = modifier,
    )
    LaunchedEffect(locations) {
        poiOverlay.removeAllItems()
        poiOverlay.addItems(
            locations.map { POIItem(it) }
        )
        mapView.invalidate() // Ensures the map is updated on screen
    }

}


private class POIItem(
    val poi: POI //FIXME add poiClass,
) : OverlayItem(poi.name, null, GeoPoint(poi.location.latitude, poi.location.longitude))

private class CurrentLocation(
    val location: Location
) : OverlayItem("current location", null, GeoPoint(location.latitude, location.longitude))
