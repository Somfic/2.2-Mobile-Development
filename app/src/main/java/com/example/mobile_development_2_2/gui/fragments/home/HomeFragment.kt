package com.example.mobile_development_2_2.gui.fragments.home

import androidx.activity.ComponentActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.mobile_development_2_2.R
import com.example.mobile_development_2_2.gui.fragments.poi.POIListFragment
import com.example.mobile_development_2_2.map.route.POI
import androidx.compose.ui.text.TextStyle as TextStyle1

/**
 * A fragment representing a list of Items.
 */
class HomeFragment : ComponentActivity() {

    @Composable
    fun HomeScreen(viewModel: HomeFragment, modifier: Modifier, helpItems: List<HelpItem>) {
        val configuration = LocalConfiguration.current

        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        Surface(
            modifier = modifier
                .fillMaxSize()
                .background(
                    Color(
                        ContextCompat
                            .getColor(
                                LocalContext.current, R.color.lightGrey
                            )
                            .dec()
                    ),
                )
        ) {
            LazyColumn {
                item {
                    Header(screenHeight.div(8).value)
                }

                item {
                    Content(helpItems = helpItems)
                }
            }


        }

    }

    @Composable
    private fun Header(height: Float) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(height.dp)
                .background(
                    Color(
                        ContextCompat
                            .getColor(
                                LocalContext.current, R.color.lightGrey
                            )
                            .dec()
                    )
                )
        ) {
            Text(
                text = "Welcome to CHLAM",
                style = TextStyle1(
                    fontSize = 30.sp,
                    color = Color(
                        ContextCompat
                            .getColor(
                                LocalContext.current, R.color.colorPrimary
                            )
                            .dec()
                    ),
                ),
                modifier = Modifier
                    .align(Alignment.Center)

            )
        }
    }

    @Composable
    private fun Content(helpItems: List<HelpItem>) {
        val configuration = LocalConfiguration.current

        val screenHeight = configuration.screenHeightDp.dp
        val screenWidth = configuration.screenWidthDp.dp

        LazyVerticalGrid(
            columns = GridCells.Adaptive(screenWidth.div(3)),
            modifier = Modifier
                .fillMaxWidth()
                .height(screenHeight.minus(screenHeight.div(5)))
                .background(
                    Color(
                        ContextCompat
                            .getColor(
                                LocalContext.current, R.color.lightGrey
                            )
                            .dec()
                    )
                ),
            // content padding
            contentPadding = PaddingValues(
                start = 12.dp,
                top = 12.dp,
                end = 12.dp,
                bottom = 16.dp
            ),
            content = {
                items(helpItems.size) { index ->
                    MessageRow(helpItems.get(index))
                }

            })
    }

    @Composable
    fun MessageRow(helpItem: HelpItem) {

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
                .clickable { }
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
                painter = painterResource(id = helpItem.imgId),
                contentDescription = null,
                contentScale = ContentScale.FillWidth,
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
                text = helpItem.title,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(bottom = 12.dp)
                    .wrapContentHeight(Alignment.Bottom)
            )
        }


    }

}