package com.example.mobile_development_2_2.gui.fragments.POIListFragment


import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
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
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mobile_development_2_2.Map.Route.POI
import com.example.mobile_development_2_2.R

class POIDetailFragment : ComponentActivity() {

    @Composable
    fun POIDetailScreen(viewModel: POIDetailFragment, modifier: Modifier, poi: POI) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            LazyColumn {
                item {
                    MessageRow1(poi)
                }
                item {
                    MessageRow2(poi)
                }
                item {
                    MessageRow3(poi)
                }

            }


        }
    }

    @Composable
    fun MessageRow1(poi: POI) {


        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().background(
                Color(
                    ContextCompat.getColor(
                        LocalContext.current, R.color.lightGrey).dec()), RectangleShape
            )
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
                    .clip(
                        RoundedCornerShape(24.dp)
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
                    .clip(
                        RoundedCornerShape(24.dp)
                    ),
                alignment = Alignment.Center,
                alpha = DefaultAlpha,
                colorFilter = null)

            Text(
                text = poi.name,
                modifier = Modifier.padding(start = 24.dp, bottom = 24.dp, top = 24.dp).align(
                    Alignment.TopCenter))

            Text(
                text = poi.streetName,
                modifier = Modifier.padding(start = 24.dp, bottom = 24.dp, top = 48.dp).align(
                    Alignment.TopCenter))

            Text(
                text = poi.description,
                modifier = Modifier.padding(start = 24.dp, bottom = 24.dp, top = 72.dp).align(
                    Alignment.TopCenter))

        }


    }

    @Composable
    fun MessageRow2(poi: POI) {
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().background(
                Color(
                    ContextCompat.getColor(
                        LocalContext.current, R.color.lightGrey).dec()), RectangleShape
            ),
            contentAlignment = Alignment.Center)
        {

        }
    }

    @Composable
    fun MessageRow3(poi: POI) {
        Box(
            modifier = Modifier.fillMaxWidth().fillMaxHeight().background(
                Color(
                    ContextCompat.getColor(
                        LocalContext.current, R.color.lightGrey).dec()), RectangleShape
            ),
            contentAlignment = Alignment.Center)
        {

        }
    }


}