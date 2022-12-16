package com.example.mobile_development_2_2.gui.fragments.poi


import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.map.route.POI

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


        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    Color(
                        ContextCompat.getColor(
                            LocalContext.current, R.color.lightGrey).dec()), RectangleShape
                )
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp)),
            elevation = 10.dp,
            backgroundColor = Color.White)
        {

            Image(
                painter = painterResource(id = poi.imgId),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    //.align(Alignment.Center)
                    .padding(start = 50.dp, end = 50.dp, top = 25.dp, bottom = 50.dp)
                    .clip(
                        RoundedCornerShape(24.dp)
                    ),
                alignment = Alignment.Center,
                alpha = DefaultAlpha,
                colorFilter = null)

            Text(
                text = poi.name,
                textAlign = TextAlign.Center, // make text center horizontal
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .wrapContentHeight(Alignment.Bottom) // align bottom
            )


            Text(
                text = poi.streetName,
                textAlign = TextAlign.Center, // make text center horizontal
                modifier = Modifier
                    .width(150.dp)
                    .height(150.dp)
                    .wrapContentHeight(Alignment.Bottom) // align bottom
            )

        }


    }

    @Composable
    fun MessageRow2(poi: POI) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                Color(
                    ContextCompat.getColor(
                        LocalContext.current, R.color.lightGrey).dec()), RectangleShape
                )
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp)),
            elevation = 10.dp,
            backgroundColor = Color.White)
        {

            TextField(
                value = poi.longDescription,
                onValueChange = {  },
                label = { Text(text = "") },
                colors = TextFieldDefaults.textFieldColors(
                    backgroundColor = Color(0xFFFFFFFF)
                ))

        }
    }

    @Composable
    fun MessageRow3(poi: POI) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .background(
                    Color(
                        ContextCompat.getColor(
                            LocalContext.current, R.color.lightGrey).dec()), RectangleShape
                )
                .padding(12.dp)
                .clip(RoundedCornerShape(12.dp)),
            elevation = 10.dp,
            backgroundColor = Color.White)
        {
            Image(
                painter = painterResource(id = poi.imgMap),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    //.align(Alignment.Center)
                    .padding(12.dp)
                    .clip(
                        RoundedCornerShape(12.dp)
                    ),
                alignment = Alignment.Center,
                alpha = DefaultAlpha,
                colorFilter = null)
        }
    }


}