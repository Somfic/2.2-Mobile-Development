package com.example.mobile_development_2_2.GUI

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.mobile_development_2_2.GUI.Fragments.FragmentInterface
import com.example.mobile_development_2_2.GUI.Fragments.HomeFragment.HomeFragment
import com.example.mobile_development_2_2.GUI.Fragments.POIListFragment.POIListFragment
import com.example.mobile_development_2_2.GUI.Fragments.RouteListFragment.RouteListFragment
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.ui.theme.MobileDevelopment2_2Theme
import org.osmdroid.config.Configuration.*


class MainActivity : ComponentActivity() {
    private val REQUEST_PERMISSIONS_REQUEST_CODE = 1
    private val homeFragment = HomeFragment()
    private val routelistFragment = RouteListFragment()
    private val poiListFragment = POIListFragment()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContent {
            MobileDevelopment2_2Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {

                    AppNavigator()
                }
            }
        }
    }

    @Composable
    fun AppNavigator() {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "homeFragment"
        ){
            composable("bottomPreview"){BottomNavigationBarPreview(navController)}
            composable("bottom"){BottomNavigationBar(navController)}
            composable("homeFragment"){MainScreen(fragmentInterface = homeFragment, navController)}
            composable("poiListFragment"){MainScreen(fragmentInterface = poiListFragment, navController)}
            composable("routeListFragment"){MainScreen(fragmentInterface = routelistFragment, navController)}
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
    fun MainScreen(fragmentInterface: FragmentInterface, navController: NavHostController) {

        Scaffold(
            topBar = { TopBar() },
            bottomBar = { BottomNavigationBar(navController) },
            content = { padding ->
                Box(modifier = Modifier.padding(padding)) {
                    fragmentInterface.StartActivity(modifier = Modifier)
                }
            },
            backgroundColor = colorResource(R.color.white)
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun MainScreenPreview() {
        HomeFragment()
    }

    @Composable
    fun TopBar() {
        val item = NavigationItem.Settings
        TopAppBar(
            title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
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

    @Composable
    fun BottomNavigationBar(navController: NavHostController) {

        val items = listOf(
            NavigationItem.Home,
            NavigationItem.Map,
            NavigationItem.POIs,
        )
        val fragments = listOf(
            "homeFragment",
            "routeListFragment",
            "poiListFragment",
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
                        var number = items.indexOf(item)
                        navController.navigate(fragments[items.indexOf(item)])
                    }
                )
            }
        }
    }


    @Composable
    fun BottomNavigationBarPreview(navController: NavHostController) {
        BottomNavigationBar(navController)
    }
}

