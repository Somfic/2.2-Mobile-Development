package com.example.mobile_development_2_2.GUI.Fragments.SettingsFragment

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
import com.example.mobile_development_2_2.GUI.Fragments.FragmentInterface
import com.example.mobile_development_2_2.GUI.Fragments.SettingsFragment.ui.theme.MobileDevelopment2_2Theme

class SettingsFragment : ComponentActivity(), FragmentInterface {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileDevelopment2_2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting4("Android")
                }
            }
        }
    }

    @Composable
    override fun StartActivity(modifier: Modifier) {
        TODO("Not yet implemented")
    }
}

@Composable
fun Greeting4(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview4() {
    MobileDevelopment2_2Theme {
        Greeting4("Android")
    }
}