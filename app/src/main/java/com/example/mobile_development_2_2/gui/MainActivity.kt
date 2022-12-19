package com.example.mobile_development_2_2.gui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.gui.fragments.home.HelpItem
import com.example.mobile_development_2_2.gui.fragments.home.HomeFragment
import com.example.mobile_development_2_2.gui.fragments.home.InfoFragment
import com.example.mobile_development_2_2.gui.fragments.poi.POIDetailFragment
import com.example.mobile_development_2_2.gui.fragments.poi.POIListFragment
import com.example.mobile_development_2_2.gui.fragments.route.RouteListFragment
import com.example.mobile_development_2_2.map.route.Route
import com.example.mobile_development_2_2.map.route.RouteManager
import com.example.mobile_development_2_2.ui.theme.MobileDevelopment2_2Theme
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState

import org.osmdroid.config.Configuration.*

class MainActivity : ComponentActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContent {
            MobileDevelopment2_2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MainScreen()
                }
            }
        }
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        val permissionsToRequest = ArrayList<String>()
        var i = 0
        while (i < grantResults.size) {
            permissionsToRequest.add(permissions[i])
            i++
        }
        if (permissionsToRequest.size > 0) {
            ActivityCompat.requestPermissions(
                this,
                permissionsToRequest.toTypedArray(),
                REQUEST_PERMISSIONS_REQUEST_CODE
            )
        }
    }

    @Composable
    fun MainScreen() {

        Scaffold(
            topBar = { TopBar() },
            bottomBar = { BottomNavigationBar() },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    var home = HomeFragment()
                    home.HomeScreen(
                        viewModel = home,
                        modifier = Modifier,
                        helpItems = HelpItem.getItems()
                    )

                    var info = InfoFragment()
                    info.InfoScreen(viewModel = info, modifier = Modifier, helpItem = HelpItem.getItems().get(0))

                    //var map = MapFragment()
                    //map.MapScreen(viewModel = map, modifier = Modifier)

                    var POIList = POIListFragment()
                    //POIList.POIListScreen(viewModel = POIList, modifier = Modifier, Route.TestRoute())

                    var POIDetail = POIDetailFragment()
                    //POIDetail.POIDetailScreen(viewModel = POIDetail, modifier = Modifier, poi = Route.testRoute().POIs.get(0))

                    var routeList = RouteListFragment()
                    //routeList.RouteListScreen(viewModel = routeList, modifier = Modifier, routes = RouteManager.TestRoutes())
                }
            },
            backgroundColor = colorResource(R.color.lightGrey)
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        MainScreen()
    }

    @Composable
    fun TopBar() {
        val item = NavigationItem.Settings
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        bottomEnd = 12.dp,
                        bottomStart = 12.dp
                    )
                )
                .background(
                    Color(
                        ContextCompat
                            .getColor(
                                LocalContext.current, R.color.lightGrey
                            )
                            .dec()
                    )
                ),
            backgroundColor = colorResource(id = R.color.colorPrimary),
            contentColor = Color.White,
            actions = {
                IconButton(onClick = { /*TODO*/ }) {
                    Icon(painterResource(id = item.icon), contentDescription = item.title)
                }
            }
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun TopBarPreview() {
        TopBar()
    }

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun BottomNavigationBar() {
        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Map,
            NavigationItem.POIs,
        )
        val premissions = rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
            )
        )
        BottomNavigation(
            backgroundColor = colorResource(id = R.color.colorPrimary),
            contentColor = Color.White,
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = 12.dp,
                        topEnd = 12.dp
                    )
                )
                .background(
                    Color(
                        ContextCompat
                            .getColor(
                                LocalContext.current, R.color.lightGrey
                            )
                            .dec()
                    )
                ),
        ) {
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painterResource(id = item.icon),
                            contentDescription = item.title
                        )
                    },
                    label = { Text(text = item.title) },
                    selectedContentColor = Color.White.copy(0.4f),
                    unselectedContentColor = Color.White,
                    alwaysShowLabel = true,
                    selected = false,
                    onClick = {
                        if (item.title.equals("Map")) {
                            premissions.launchMultiplePermissionRequest()
                        }
                    }
                )
            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun BottomNavigationBarPreview() {
        BottomNavigationBar()
    }
}

