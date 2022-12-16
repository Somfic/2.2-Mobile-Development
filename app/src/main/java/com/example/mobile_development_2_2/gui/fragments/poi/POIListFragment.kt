package com.example.mobile_development_2_2.gui.fragments.poi


import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.SnackbarDefaults.backgroundColor
import androidx.compose.material.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.BlendMode.Companion.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.map.route.Route
import org.osmdroid.util.GeoPoint

/**
 * A fragment representing a list of Items.
 */
class POIListFragment : ComponentActivity() {

    @Composable
    fun POIListScreen(viewModel: POIListFragment, modifier: Modifier, route: Route) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            LazyColumn {
                items(route.POIs) { poi ->
                    MessageRow(poi)
                }

            }


        }

    }

    @Composable
    fun MessageRow(poi: POI) {


        Box(
            modifier = Modifier.fillMaxWidth().height(150.dp).background(Color(ContextCompat.getColor(LocalContext.current, R.color.lightGrey).dec()), RectangleShape)
                .clickable {

                },
            contentAlignment = Alignment.Center)
        {

            Image(
                painter = painterResource(id = R.drawable.img_whtsqr),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .align(Alignment.Center)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(24.dp)
                    ),
                alignment = Alignment.Center,
                alpha = DefaultAlpha,
                colorFilter = null)

            Image(
                painter = painterResource(id = poi.imgId),
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .padding(start = 24.dp, bottom = 24.dp, top = 24.dp)
                    .clip(RoundedCornerShape(24.dp)
                    ),
                alignment = Alignment.Center,
                alpha = DefaultAlpha,
                colorFilter = null)

            Text(
                text = poi.name,
                modifier = Modifier.padding(start = 24.dp, bottom = 24.dp, top = 24.dp).align(Alignment.TopCenter))

            Text(
                text = poi.streetName,
                modifier = Modifier.padding(start = 24.dp, bottom = 24.dp, top = 48.dp).align(Alignment.TopCenter))

            Text(
                text = poi.description,
                modifier = Modifier.padding(start = 24.dp, bottom = 24.dp, top = 72.dp).align(Alignment.TopCenter))

        }



    }



    fun TestRoute(): Route {
        val avans = POI(
            name = "Avans",
            location = GeoPoint(51.5856, 4.7925),
            imgId = R.drawable.img_poi1,
            streetName = "street1",
            description = "description of Avans"
        )

        // TODO: Move to POI repository
        val breda = POI(
            name = "Breda",
            location = GeoPoint(51.5719, 4.7683),
            imgId = R.drawable.img_poi2,
            streetName = "street2",
            description = "description of Breda"
        )

        // TODO: Move to POI repository
        val amsterdam = POI(
            name = "Amsterdam",
            location = GeoPoint(52.3676, 4.9041),
            imgId = R.drawable.img_poi3,
            streetName = "street3",
            description = "description of Amsterdam"
        )

        // TODO: Move to POI repository
        val cities = listOf(
            avans,
            breda,
            amsterdam,
            avans,
            breda,
            amsterdam,
            avans,
            breda,
            amsterdam,
            avans,
            breda,
            amsterdam,
            avans,
            breda,
            amsterdam,
            avans,
            breda,
            amsterdam,
        )


        return Route("testRoute", "a test route", cities)

    }

}

