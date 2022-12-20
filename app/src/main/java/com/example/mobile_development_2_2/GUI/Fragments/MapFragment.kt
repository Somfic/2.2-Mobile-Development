package com.example.mobile_development_2_2.gui.fragments

import android.Manifest
import android.content.Context
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.location.Location

import androidx.compose.foundation.background

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.Icon
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.scale
import androidx.core.location.LocationManagerCompat.requestLocationUpdates
import androidx.lifecycle.viewModelScope

import com.example.mobile_development_2_2.R

import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.ui.viewmodels.OSMViewModel
import com.google.accompanist.permissions.MultiplePermissionsState
import com.example.mobile_development_2_2.map.route.Route
import com.example.mobile_development_2_2.map.route.RouteManager
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.*
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.bonuspack.kml.KmlGeometry
import org.osmdroid.bonuspack.kml.Style
import org.osmdroid.api.IMapController
import org.osmdroid.bonuspack.kml.LineStyle
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.FolderOverlay
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.gridlines.LatLonGridlineOverlay.lineColor
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment() : LocationListener {


    lateinit var myLocation: MyLocationNewOverlay
    lateinit var mapView: MapView
    lateinit var context: Context


    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun MapScreen(viewModel: OSMViewModel, modifier: Modifier) {
        viewModel.provider.locationListener = this
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


            }
            OSM(

                modifier = modifier,
                locations = viewModel.pois,
                routePoints = viewModel.pois.map { it.location }.toMutableList(),
                provider = viewModel.provider,
                followRoute = true
            )
            if (!premissions.allPermissionsGranted) {
                Column() {

                    Text(text = "No Location Premission granted", color = Color.Red)
                }
            }
            Row() {


                Text(
                    text = "Â© OpenStreetMap contributors",
                    fontSize = 8.sp,
                    modifier = Modifier
                        .background(Color.White, RectangleShape)
                        .align(Alignment.Bottom)
                )


            }
        }
    }


    @Composable
    private fun OSM(

        modifier: Modifier = Modifier,
        locations: List<POI> = listOf(),
        routePoints: MutableList<GeoPoint> = mutableListOf(),
        provider: IMyLocationProvider,
        followRoute: Boolean,
    ) {

        val listener = object : ItemizedIconOverlay.OnItemGestureListener<POIItem> {
            override fun onItemSingleTapUp(index: Int, item: POIItem?): Boolean {
                if (item != null) {
                    clickedOnPoi(item.poi)
                }
                return true
            }

            override fun onItemLongPress(index: Int, item: POIItem?): Boolean {
                if (item != null) {
                    longClickOnPoi(item.poi)
                }
                return false
            }
        }
        context = LocalContext.current


        val mapView = remember {
            MapView(context)
        }
        this.mapView = mapView


        //FIXME poilayer kan toegevoegd worden als er point of interest zijn
        val poiOverlay = remember {
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
        val visitedOverlay = remember {
            ItemizedIconOverlay(
                mutableListOf<POIItem>(),
                ContextCompat.getDrawable(
                    context,
                    org.osmdroid.library.R.drawable.person
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
            MyLocationNewOverlay(provider, mapView)
        }
        this.myLocation = myLocation
        myLocation.enableMyLocation()



        AndroidView(
            factory = {
                mapView.apply {
                    setTileSource(TileSourceFactory.MAPNIK)
                    isTilesScaledToDpi = true
                    controller.setCenter(GeoPoint(51.58703, 4.773187)) // Avans
                    controller.setZoom(17.0)


                    //mapView.overlays.add(poiOverlay)


                    mapView.overlays.add(poiOverlay)
                    mapView.overlays.add(visitedOverlay)

                    mapView.overlays.add(myLocation)

                    mapView.overlays.add(currentRoute)


                }
            },
            modifier = modifier,
            update = {
                if (followRoute) {
                    myLocation.disableFollowLocation()
                    myLocation.enableFollowLocation()

                } else {
                    myLocation.disableFollowLocation()
                    mapView.mapOrientation = 0f

                }
            }

        )
        LaunchedEffect(locations) {
            poiOverlay.removeAllItems()
            poiOverlay.addItems(
                locations.filter { !it.visited }.map { POIItem(it) }
            )
            mapView.invalidate() // Ensures the map is updated on screen
        }
        LaunchedEffect(locations) {
            visitedOverlay.removeAllItems()
            visitedOverlay.addItems(
                locations.filter { it.visited }.map { POIItem(it) }
            )
            mapView.invalidate() // Ensures the map is updated on screen
        }
        if (followRoute) {
            Row(verticalAlignment = Alignment.Bottom) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {


                    Button(
                        onClick = { myLocation.enableFollowLocation() },
                        modifier = Modifier
                            .size(50.dp)
                            .align(Alignment.End),
                    ) {

                    }
                }
            }
        }

    }

    fun setRoute(route: String?) {



        val resources = mapView.resources


//        val inputStream = resources.openRawResource(R.raw.test_route)
        val inputStream = when (route) {
            "test_Route1" -> resources.openRawResource(R.raw.test_route)
            "testRoute2" -> resources.openRawResource(R.raw.test_route2)
            "testRoute3" -> resources.openRawResource(R.raw.test_route3)
            else -> resources.openRawResource(R.raw.test_route)
        }
//        val inputStream = resources.openRawResource(R.raw.test_route)

        val kmldocument = KmlDocument()
        kmldocument.parseGeoJSONStream(inputStream)
        val kmlIcon = BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.marker_node))

        val klmstyle = Style(
           kmlIcon.bitmap,Color.Red.hashCode(),20f,Color.White.hashCode())


            //	public Style(Bitmap icon, int lineColor, float lineWidth, int fillColor){ )


//        klmstyle.mLineStyle = LineStyle(0,10f)

        val feature = kmldocument.mKmlRoot.buildOverlay(mapView,klmstyle,null,kmldocument);
       
        mapView.overlays.add(feature)
        mapView.invalidate()


    }

    private fun clickedOnPoi(poi: POI) {
        //todo
    }

    private fun longClickOnPoi(poi: POI) {
        //todo
    }

    override fun onLocationChanged(p0: Location) {

        if (myLocation.isFollowLocationEnabled) {
            mapView.mapOrientation = 360 - p0.bearing
            mapView.controller.setZoom(17.0)
            mapView.setMapCenterOffset(0, 600)
            myLocation.setDirectionIcon(
                ContextCompat.getDrawable(
                    context,
                    org.osmdroid.library.R.drawable.round_navigation_white_48
                )!!.toBitmap(150, 150)
            )

        } else {
            mapView.mapOrientation = 0f
            mapView.setMapCenterOffset(0, 0)
            myLocation.setDirectionIcon(
                ContextCompat.getDrawable(
                    context,
                    org.osmdroid.library.R.drawable.round_navigation_white_48
                )!!.toBitmap(150, 150)
            )
        }
        //myLocation.enableFollowLocation()
        mapView.invalidate()
    }

}

private class POIItem(
    val poi: POI //FIXME add poiClass,
) : OverlayItem(poi.name, null, GeoPoint(poi.location.latitude, poi.location.longitude))
