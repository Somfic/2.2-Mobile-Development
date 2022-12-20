package com.example.mobile_development_2_2.gui.fragments.route

import android.Manifest
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.data.Lang
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.map.route.Route
import com.example.mobile_development_2_2.map.route.RouteManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState


@Composable
fun RouteListScreen(
    modifier: Modifier, routes: List<Route>, onRouteClicked: () -> Unit, onPOIClicked: () -> Unit
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(routes) { route ->
                MessageRow(route, onRouteClicked, onPOIClicked)
            }

        }
    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun MessageRow(route: Route, onRouteClicked: () -> Unit, onPOIClicked: () -> Unit) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(500.dp)
            .background(
                Color(
                    ContextCompat
                        .getColor(
                            LocalContext.current, R.color.lightGrey
                        )
                        .dec()
                ), RectangleShape
            )
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp)),
        elevation = 10.dp,
        backgroundColor = Color.White
    ) {

        Image(
            painter = painterResource(id = route.imgId),
            contentDescription = null,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(start = 8.dp, end = 8.dp, top = 8.dp, bottom = 150.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                ),
            alignment = Alignment.Center,
            alpha = DefaultAlpha,
            colorFilter = null
        )

        Text(
            text = route.name,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(bottom = 110.dp, top = 24.dp)
                .wrapContentHeight(Alignment.Bottom),
            fontSize = 30.sp
        )

        Text(
            text = Lang.get(R.string.route_distance) + ": ${route.length}m",
            textAlign = TextAlign.Start,
            modifier = Modifier
                .padding(bottom = 80.dp, top = 80.dp, start = 12.dp)
                .wrapContentHeight(Alignment.Bottom)
        )

        Text(
            text = Lang.get(R.string.routes_waypoints) + ": ${route.POIs.size}",
            textAlign = TextAlign.End,
            modifier = Modifier
                .padding(bottom = 80.dp, top = 80.dp, end = 12.dp)
                .wrapContentHeight(Alignment.Bottom)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(35.dp),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(
                onClick = {
                    RouteManager.selectItem(route)
                    onPOIClicked()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red, contentColor = Color.White
                ), modifier = Modifier
                    .width(150.dp)
                    .height(35.dp)
                    .offset(-100.dp, -25.dp)
            ) {
                Text(text = Lang.get(R.string.routes_waypoints))
            }

            val premissions = rememberMultiplePermissionsState(
                listOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION,
                )
            )
            Button(
                onClick = {
                    RouteManager.selectItem(route)
                    premissions.launchMultiplePermissionRequest()
                    onRouteClicked()
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color.Red, contentColor = Color.White
                ), modifier = Modifier
                    .width(150.dp)
                    .height(35.dp)
                    .offset(100.dp, -25.dp)
            ) {
                Text(text =  Lang.get(R.string.routes_map))
            }
        }
    }


}



