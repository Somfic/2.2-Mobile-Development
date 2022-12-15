package com.example.mobile_development_2_2

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile_development_2_2.ui.theme.MobileDevelopment2_2Theme
import com.example.mobile_development_2_2.ui.viewmodels.MapFragment

import android.preference.PreferenceManager
import androidx.core.app.ActivityCompat


import org.osmdroid.config.Configuration.*

class MainActivity : ComponentActivity() {
    private val RequestCode = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this))
        setContent {
            MobileDevelopment2_2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    MapFragment().MapScreen(viewModel = MapFragment(), modifier = Modifier )
                }
            }
        }
    }
    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
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
                RequestCode)
        }
    }
}



@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MobileDevelopment2_2Theme {
        Greeting("Android")
    }
}