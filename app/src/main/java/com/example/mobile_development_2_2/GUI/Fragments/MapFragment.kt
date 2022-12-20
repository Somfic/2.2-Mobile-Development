package com.example.mobile_development_2_2.gui.fragments

import android.Manifest
import android.content.Context
import android.location.Location

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
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
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapController
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import kotlin.math.roundToInt


class MapFragment() : LocationListener {


    lateinit var myLocation: MyLocationNewOverlay
    lateinit var mapView: MapView
    lateinit var context: Context
    var followRoute by mutableStateOf(false)
    lateinit var route: Route

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun MapScreen(viewModel: OSMViewModel, modifier: Modifier, onPOIClicked :() -> Unit) {
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
                route = viewModel.route,
                modifier = modifier,
                locations = viewModel.pois,
                routePoints = viewModel.pois.map { it.location }.toMutableList(),
                provider = viewModel.provider,
                onPOIClicked = onPOIClicked
            )
            if (!premissions.allPermissionsGranted) {
                Column() {

                    Text(text = "No Location Premission granted", color = Color(ContextCompat.getColor(context,R.color.colorPrimary).dec()) )
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
            if(!followRoute){

                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = { followRoute = true },
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(
                                ContextCompat.getColor(
                                    LocalContext.current, R.color.colorPrimary
                                ).dec()), contentColor = Color.White
                        )
                    ) {
                        if(route.hasProgress())
                            Text(text = "Start route")
                        else
                            Text(text = "Resume route")
                    }

                }
            }
            if (followRoute) {
                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = { myLocation.enableFollowLocation() },
                        modifier = Modifier
                            .padding(bottom = 20.dp, end = 30.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(
                                ContextCompat.getColor(
                                    LocalContext.current, R.color.colorPrimary
                                ).dec()), contentColor = Color.White
                        )
                    ) {
                        Text(text = "Recenter")
                    }

                }
                
                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
                    Button(
                        onClick = { followRoute = false },
                        modifier = Modifier
                            .padding(top = 20.dp, start = 30.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(
                                ContextCompat.getColor(
                                    LocalContext.current, R.color.colorPrimary
                                ).dec()), contentColor = Color.White
                        )

                    ) {
                        Icon(painter = painterResource(id = R.drawable.ic_baseline_close_24), contentDescription = "")
                    }

                }
            }
        }
    }


    @Composable
    private fun OSM(

        modifier: Modifier = Modifier,
        locations: List<POI> = listOf(),
        route: Route,
        routePoints: MutableList<GeoPoint> = mutableListOf(),
        provider: IMyLocationProvider,
        onPOIClicked :() -> Unit,
    ) {
        this.route = route
        val listener = object : ItemizedIconOverlay.OnItemGestureListener<POIItem> {
            override fun onItemSingleTapUp(index: Int, item: POIItem?): Boolean {
                if (item != null) {
                    clickedOnPoi(item.poi,onPOIClicked )
                }
                return true
            }

            override fun onItemLongPress(index: Int, item: POIItem?): Boolean {
                if (item != null) {
                    longClickOnPoi(item.poi, onPOIClicked)
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
                    controller.setCenter(GeoPoint(51.5856, 4.7925)) // Avans
                    controller.setZoom(17.0)


                    //mapView.overlays.add(poiOverlay)


                    mapView.overlays.add(poiOverlay)
                    mapView.overlays.add(visitedOverlay)

                    mapView.overlays.add(myLocation)

                    mapView.overlays.add(currentRoute)
                    val kmldocument = KmlDocument()
                    //get data/routes/historische_kilometer.geojson

                    val resources = getResources()
                    //switch case voor verschillende routes

                    val inputStream = resources.openRawResource(R.raw.test_route)
                    kmldocument.parseGeoJSONStream(inputStream)

                    val klmstyle = kmldocument.getStyle("route")


                    val feature = kmldocument.mKmlRoot.buildOverlay(mapView,klmstyle,null,kmldocument);
                    mapView.overlays.add(feature)
                    mapView.invalidate()

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





    }

    private fun clickedOnPoi(poi: POI, onPOIClicked :() -> Unit) {
        RouteManager.selectPOI(poi)
        onPOIClicked()
    }

    private fun longClickOnPoi(poi: POI , onPOIClicked :() -> Unit) {
        //todo
    }

    override fun onLocationChanged(p0: Location) {

        if (myLocation.isFollowLocationEnabled) {
            mapView.mapOrientation = 360 - p0.bearing
            mapView.controller.setZoom(17.0)
            mapView.setMapCenterOffset(0, 500)
            myLocation.isDrawAccuracyEnabled = false

        } else {
            mapView.mapOrientation = 0f
            mapView.setMapCenterOffset(0, 0)
            myLocation.isDrawAccuracyEnabled = true
        }
        mapView.invalidate()
    }

}

private class POIItem(
    val poi: POI //FIXME add poiClass,
) : OverlayItem(poi.name, null, GeoPoint(poi.location.latitude, poi.location.longitude))
