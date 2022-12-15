package com.example.mobile_development_2_2.ui.viewmodels

import android.Manifest
import android.location.Location
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.data.LocationProvider
import com.google.accompanist.permissions.rememberMultiplePermissionsState
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
    fun MapScreen(viewModel: MapFragment, modifier: Modifier) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            val viewmodel = ViewModelMap()
            val premissions = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )

            if (premissions.allPermissionsGranted) {
                OSM(

                    modifier = modifier,
                    locations = viewmodel,
                    routePoints = viewmodel.map { it.location }.toMutableList()

                )
            } else {
                noPremmisions()
                OSM(

                )
            }
        }
    }

}
//TODO DELETE And make other button do the work

@Composable
private fun noPremmisions() {
    Column() {
        Text(text = "No Location Premission granted")
    }
}


//TODO DELETE And make a list provider
private fun ViewModelMap(): List<POI> {
    val avans = POI(
        name = "Avans",
        location = GeoPoint(51.5856, 4.7925),
    )

    // TODO: Move to POI repository
    val breda = POI(
        name = "Breda",
        location = GeoPoint(51.5719, 4.7683),
    )

    // TODO: Move to POI repository
    val amsterdam = POI(
        name = "Amsterdam",
        location = GeoPoint(52.3676, 4.9041),
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
    currentLocation: GeoPoint? = null,
) {



    val context = LocalContext.current
    val locationProvider = LocationProvider(context)


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
            ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.ic_menu_mylocation),
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
        val thisLocation = currentLocation ?: GeoPoint(0, 0)
        IconOverlay(
            thisLocation,
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