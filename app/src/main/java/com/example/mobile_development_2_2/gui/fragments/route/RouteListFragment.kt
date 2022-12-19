package com.example.mobile_development_2_2.gui.fragments.route

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
import com.example.mobile_development_2_2.map.route.POI
import com.example.mobile_development_2_2.map.route.Route

/**
 * A fragment representing a list of Items.
 */
class RouteListFragment : ComponentActivity() {

    @Composable
    fun RouteListScreen(viewModel: RouteListFragment, modifier: Modifier, routes: List<Route>) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            LazyColumn {
                items(routes) { route ->
                    MessageRow(route)
                }

            }


        }

    }

    @Composable
    fun MessageRow(route: Route) {

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
            backgroundColor = Color.White)
        {

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
                colorFilter = null)

            Text(
                text = route.name,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 110.dp, top = 24.dp)
                    .wrapContentHeight(Alignment.Bottom),
                fontSize = 30.sp)

            Text(
                text = "Distance: ${route.length} meters",
                textAlign = TextAlign.Start,
                modifier = Modifier
                    .padding(bottom = 80.dp, top = 80.dp, start = 12.dp)
                    .wrapContentHeight(Alignment.Bottom))

            Text(
                text = "Points: ${route.POIs.size}",
                textAlign = TextAlign.End,
                modifier = Modifier
                    .padding(bottom = 80.dp, top = 80.dp, end = 12.dp)
                    .wrapContentHeight(Alignment.Bottom))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(35.dp),
                contentAlignment = Alignment.BottomCenter
            ) {
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White),
                    modifier = Modifier
                        .width(150.dp)
                        .height(35.dp)
                        .offset(-100.dp, -25.dp)) {
                    Text(text = "Points")
                }
                Button(
                    onClick = {  },
                    colors = ButtonDefaults.buttonColors(backgroundColor = Color.Red, contentColor = Color.White),
                    modifier = Modifier
                        .width(150.dp)
                        .height(35.dp)
                        .offset(100.dp, -25.dp)) {
                    Text(text = "Map")
                }
            }



        }







    }
}