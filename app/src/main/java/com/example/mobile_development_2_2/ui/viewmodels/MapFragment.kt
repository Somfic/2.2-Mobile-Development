package com.example.mobile_development_2_2.ui.viewmodels

import android.location.Location
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.IconOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline


class MapFragment{


    @Composable
    fun MapScreen(viewModel: MapFragment, modifier: Modifier) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            val viewmodel = ViewModelMap()
            OSM(

                modifier = modifier,
                locations = viewmodel
            )
        }

    }
    //TODO DELETE And make a list provider
    private fun ViewModelMap() : List<POI>{
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
        locations: List<POI> = listOf()
    ) {
        val context = LocalContext.current
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
            ItemizedIconOverlay(context, mutableListOf<POIItem>(), listener)
        }
        val myLocation = remember(mapView) {
            val location: Location = Location("s")

            IconOverlay( (GeoPoint(51.5856, 4.7925)),
                ContextCompat.getDrawable(context, org.osmdroid.library.R.drawable.ic_menu_mylocation))

        }
        val routeOverlay = remember {
            Polyline()
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
                    mapView.overlays.add(myLocation)
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
}


private class POIItem(
    val poi: POI //FIXME add poiClass,
): OverlayItem(poi.name, null, GeoPoint(poi.location.latitude, poi.location.longitude))
private class CurrentLocation(
    val location: Location
): OverlayItem("current location", null, GeoPoint(location.latitude, location.longitude))