package com.example.mobile_development_2_2.gui.fragments

import android.Manifest
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*

import androidx.compose.runtime.*

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.data.Lang
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.map.route.Route
import com.example.mobile_development_2_2.map.route.RouteManager
import com.example.mobile_development_2_2.ui.viewmodels.OSMViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.android.gms.location.*
import kotlinx.coroutines.flow.*
import org.osmdroid.bonuspack.kml.KmlDocument
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem
import org.osmdroid.views.overlay.Polyline
import org.osmdroid.views.overlay.mylocation.IMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay


class MapFragment : LocationListener {


    lateinit var myLocation: MyLocationNewOverlay
    lateinit var mapView: MapView
    lateinit var context: Context
    lateinit var route: Route
    //var followRoute by mutableStateOf(this.route.started)

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
                modifier = modifier,
                routePoints = viewModel.pois.map { it.location }.toMutableList(),
                provider = viewModel.provider,
                onPOIClicked = onPOIClicked
            )
            if (!premissions.allPermissionsGranted) {
                Column {

                    Text(text = Lang.get(R.string.map_no_location_permission), color = MaterialTheme.colors.error)
                }
            }
            Row {
                Text(
                    text = Lang.get(R.string.map_copyright),
                    fontSize = 8.sp,
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface, RectangleShape)
                        .align(Alignment.Bottom)
                )
            }
            if(!RouteManager.getRouteManager(null).getSelectedRoute().started.value){

                Row(verticalAlignment = Alignment.Bottom, horizontalArrangement = Arrangement.Center) {
                    Button(
                        onClick = {
                            Log.d("f", "" + route.started.value)
                            RouteManager.getRouteManager(context).setRouteState(true);
                            Log.d("f", "" + route.started.value)
                                  },
                        modifier = Modifier
                            .padding(bottom = 20.dp),
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(
                                ContextCompat.getColor(
                                    LocalContext.current, R.color.colorPrimary
                                ).dec()), contentColor = Color.White
                        )
                    ) {
                        if(!route.hasProgress())
                            Text(text = Lang.get(R.string.map_start))
                        else
                            Text(text = Lang.get(R.string.map_continue))
                    }

                }
            }
            if (route.started.value) {
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
                        Text(text = Lang.get(R.string.map_recenter))
                    }

                }

                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.Start) {
                    Button(
                        onClick = {
                            RouteManager.getRouteManager(context).setRouteState(false); },
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

                Row(verticalAlignment = Alignment.Top, horizontalArrangement = Arrangement.End) {
                    Card(modifier = Modifier
                        .padding(top = 20.dp, end = 30.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .height(60.dp)
                        .width(120.dp),
                        elevation = 10.dp,
                        backgroundColor = Color(
                            ContextCompat
                                .getColor(
                                    LocalContext.current, R.color.colorPrimary
                                )
                                .dec()
                        )
                    ) {
                        Text(
                            text = "" + route.totalPoisVisited.value + " / " + route.POIs.size + " points",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.wrapContentHeight(Alignment.Top)
                                .padding(top = 6.dp),
                            color = Color.White

                        )
                        Text(
                            text ="" + route.currentLength.value + " / " + route.length + " km",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.wrapContentHeight(Alignment.Bottom)
                                .padding(bottom = 8.dp),
                            color = Color.White

                        )
                    }

                }

            }
        }
    }


    @Composable
    private fun OSM(

        modifier: Modifier = Modifier,
        routePoints: MutableList<GeoPoint> = mutableListOf(),
        provider: IMyLocationProvider,
        onPOIClicked :() -> Unit,
    ) {
        this.route = RouteManager.getRouteManager(null).getSelectedRoute()
        val locations = route.POIs
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
        currentRoute.outlinePaint.color = MaterialTheme.colors.primary.toArgb()

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


                    val feature = kmldocument.mKmlRoot.buildOverlay(mapView,klmstyle,null,kmldocument)
                    mapView.overlays.add(feature)
                    mapView.invalidate()

                }
            },
            modifier = modifier,
            update = {
                if (route.started.value) {
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
        RouteManager.getRouteManager(context).selectPOI(poi)
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
