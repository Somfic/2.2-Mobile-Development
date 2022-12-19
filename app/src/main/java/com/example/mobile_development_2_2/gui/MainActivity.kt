package com.example.mobile_development_2_2.gui

import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
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
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.gui.fragments.home.HelpItem
import com.example.mobile_development_2_2.gui.fragments.home.HomeScreen
import com.example.mobile_development_2_2.gui.fragments.home.InfoScreen
import com.example.mobile_development_2_2.gui.fragments.poi.POIDetailScreen
import com.example.mobile_development_2_2.gui.fragments.poi.POIListScreen
import com.example.mobile_development_2_2.gui.fragments.route.RouteListScreen
import com.example.mobile_development_2_2.map.route.RouteManager
import com.example.mobile_development_2_2.data.GetLocationProvider
import com.example.mobile_development_2_2.data.LocationProvider
import com.example.mobile_development_2_2.ui.theme.MobileDevelopment2_2Theme

import com.example.mobile_development_2_2.ui.viewmodels.OSMViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.currentCoroutineContext
import org.osmdroid.config.Configuration.*

class MainActivity : ComponentActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    lateinit var osmViewModel: OSMViewModel

    enum class Fragments(@StringRes val title: Int) {
        Home(title = R.string.homeScreen),
        Info(title = R.string.infoScreen),
        POIList(title = R.string.poiListScreen),
        POI(title = R.string.POIScreen),
        Route(title = R.string.routeScreen),
        Map(title = R.string.mapScreen),
        Settings(title = R.string.settingsScreen),
    }

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
    fun MainScreen(navController: NavHostController = rememberNavController()) {
        // Get current back stack entry
        val backStackEntry by navController.currentBackStackEntryAsState()

        val context = LocalContext.current
        val osmViewModel = remember {
            OSMViewModel(GetLocationProvider(LocationProvider(context = context)),  this)
        }
        this.osmViewModel = osmViewModel

        // Get the name of the current screen
        val currentScreen = Fragments.valueOf(
            backStackEntry?.destination?.route ?: Fragments.Home.name
        )

        Scaffold(
            topBar = {
                TopBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() })
            },
            bottomBar = { BottomNavigationBar(
                onHomeButtonClicked = {navController.navigate(Fragments.Home.name)},
                onHomePOIClicked = {navController.navigate(Fragments.POIList.name)},
                onMapButtonClicked = {navController.navigate(Fragments.Route.name)
                    Log.d("123", "map")}
            ) },
            backgroundColor = colorResource(R.color.lightGrey)
        ) { innerpadding ->
            NavHost(
                navController = navController,
                startDestination = Fragments.Home.name,
                modifier = Modifier.padding(innerpadding)
            ) {
                composable(route = Fragments.Home.name) {
                    HomeScreen(
                        modifier = Modifier,
                        helpItems = HelpItem.getItems(),
                        onPOIButtonClicked = {
                            navController.navigate(Fragments.Info.name)
                        })

                }
                composable(route = Fragments.Route.name) {
                    RouteListScreen(
                        modifier = Modifier,
                        routes = RouteManager.TestRoutes(),
                        onRouteClicked = {
                            navController.navigate(Fragments.Map.name)
                        },
                        onPOIClicked = {
                            navController.navigate(Fragments.POIList.name)
                        }
                    )
                }
                composable(route = Fragments.POIList.name) {
                    POIListScreen(
                        modifier = Modifier,
                        route = RouteManager.getSelectedRoute(),
                        onPOIClicked = {
                            navController.navigate(Fragments.POI.name)
                        }
                    )
                }
                composable(route = Fragments.Info.name){
                    InfoScreen(
                        modifier = Modifier,
                        helpItem = HelpItem.getSelectedItem()
                    )
                }
                composable(route = Fragments.POI.name){
                    POIDetailScreen(
                        modifier = Modifier,
                        poi = RouteManager.getSelectedPOI()
                    )
                }

            }
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        MainScreen()
    }

    @Composable
    fun TopBar(
        currentScreen: Fragments,
        canNavigateBack: Boolean,
        navigateUp: () -> Unit,
        modifier: Modifier = Modifier
    ) {
        val item = NavigationItem.Settings
        TopAppBar(
            title = { Text(stringResource(currentScreen.title)) },
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

            },
            navigationIcon = {
                if (canNavigateBack) {
                    IconButton(onClick = navigateUp) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = ""
                        )
                    }
                }
            }
        )
    }

/*    @Preview(showBackground = true)
    @Composable
    fun TopBarPreview() {
        TopBar(true, {})
    }*/

    @OptIn(ExperimentalPermissionsApi::class)
    @Composable
    fun BottomNavigationBar(
        onHomeButtonClicked: () -> Unit,
        onMapButtonClicked: () -> Unit,
        onHomePOIClicked: () -> Unit
    ) {
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
                var onClick = onHomeButtonClicked

                if(item.title.equals("Map")){
                    onClick = onMapButtonClicked

                } else if(item.title.equals("Home")){
                    onClick = onHomeButtonClicked

                } else if(item.title.equals("POIs")){
                    onClick = onHomePOIClicked

                }


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
                    onClick = onClick
                    //premissions.launchMultiplePermissionRequest()
                )
            }
        }
    }

/*    @Preview(showBackground = true)
    @Composable
    fun BottomNavigationBarPreview() {
        BottomNavigationBar()
    }*/
}
