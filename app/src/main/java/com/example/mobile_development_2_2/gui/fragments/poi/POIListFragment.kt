@file:OptIn(ExperimentalMaterialApi::class)

package com.example.mobile_development_2_2.gui.fragments.poi


import android.app.Application
import android.graphics.drawable.Drawable
import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterStart
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.map.route.Route
import com.example.mobile_development_2_2.map.route.RouteManager
import org.osmdroid.util.GeoPoint


@Composable
fun POIListScreen(modifier: Modifier, route: Route, onPOIClicked: () -> Unit) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        LazyColumn {
            items(route.POIs) { poi ->
                MessageRow(poi, onPOIClicked)
            }

        }


    }

}

@Composable
fun MessageRow(poi: POI, onPOIClicked: () -> Unit) {

    Card(
        onClick = {
            RouteManager.selectPOI(poi)
            onPOIClicked()
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(
                MaterialTheme.colors.surface, RectangleShape
            )
            .padding(12.dp)
            .clip(RoundedCornerShape(12.dp)),
        elevation = 10.dp,
        backgroundColor = MaterialTheme.colors.surface
    )
    {
        val application = LocalContext.current.applicationContext as Application
        val imageStream = application.assets.open(poi.img)
        val imageDrawable = Drawable.createFromStream(imageStream, null)
        Image(
            bitmap = imageDrawable!!.toBitmap().asImageBitmap(),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .padding(24.dp)
                .clip(
                    RoundedCornerShape(12.dp)
                ),
            alignment = Alignment.CenterStart,
            alpha = DefaultAlpha,
            colorFilter = null
        )

        Text(
            text = poi.name,
            modifier = Modifier
                .padding(start = 24.dp, bottom = 24.dp, top = 24.dp)
                .offset(x = 190.dp)
        )

        Text(
            text = poi.streetName,
            modifier = Modifier
                .padding(start = 24.dp, bottom = 24.dp, top = 48.dp)
                .offset(x = 190.dp)
        )

        TextField(
            value = poi.shortDescription,
            modifier = Modifier
                .padding(start = 200.dp, bottom = 24.dp, top = 65.dp, end = 24.dp)
                .width(50.dp),
            readOnly = true,
            onValueChange = { },
            label = { Text(text = "") },
        )

    }
}






