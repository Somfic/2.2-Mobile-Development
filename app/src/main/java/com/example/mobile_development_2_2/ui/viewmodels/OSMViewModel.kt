package com.example.mobile_development_2_2.ui.viewmodels

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Overlay


class OSMViewModel {
    lateinit var overlays: List<Overlay>

    @Composable
    fun MapScreen(viewModel: OSMViewModel, modifier: Modifier) {
        Surface(
            modifier = modifier.fillMaxSize()
        ) {
            OSM(
                modifier = modifier
            )
        }

    }

    @Composable
    private fun OSM(
        modifier: Modifier = Modifier
    ) {
        val context = LocalContext.current
        val mapView = remember {
            MapView(context)
        }

        AndroidView(
            factory = {
                mapView.apply {
                    isTilesScaledToDpi = true
                    controller.setCenter(GeoPoint(51.5856, 4.7925)) // Avans
                    controller.setZoom(17.0)
                }
            },
            modifier = modifier,
        )
    }

}