package com.example.mobile_development_2_2.gui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat

import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.data.GetLocationProvider
import com.example.mobile_development_2_2.gui.fragments.poi.POIDetailFragment
import com.example.mobile_development_2_2.gui.fragments.poi.POIListFragment
import com.example.mobile_development_2_2.data.LocationProvider

import com.example.mobile_development_2_2.gui.fragments.MapFragment
import com.example.mobile_development_2_2.gui.fragments.home.HomeFragment
import com.example.mobile_development_2_2.gui.fragments.route.RouteListFragment
import com.example.mobile_development_2_2.ui.theme.MobileDevelopment2_2Theme

import com.example.mobile_development_2_2.ui.viewmodels.OSMViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.currentCoroutineContext
import org.osmdroid.config.Configuration.*

class MainActivity : ComponentActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private val homeFragment = HomeFragment()
    private val routelistFragment = RouteListFragment()
    private val poiListFragment = POIListFragment()
    lateinit var osmViewModel: OSMViewModel

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

        val context = LocalContext.current
        val osmViewModel = remember {
            OSMViewModel(GetLocationProvider(LocationProvider(context = context)),  this)
        }
        osmViewModel.AddGeofence(51.5856, 4.7925)
        this.osmViewModel = osmViewModel

        Scaffold(
            topBar = { TopBar() },
            bottomBar = { BottomNavigationBar() },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    var map = MapFragment()
                    map.MapScreen(viewModel = osmViewModel, modifier = Modifier)
                }
            },
            backgroundColor = colorResource(R.color.black)
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
            title = { Text(text = stringResource(R.string.project_name), fontSize = 18.sp) },
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
            contentColor = Color.White
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
                        if (item.title.equals("Map")){
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
