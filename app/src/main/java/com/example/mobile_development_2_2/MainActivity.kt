package com.example.mobile_development_2_2

import android.content.Context
import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.mobile_development_2_2.ui.theme.MobileDevelopment2_2Theme
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileDevelopment2_2Theme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    //Greeting(getString(R.string.app_name))
                    ShowLocale(this, getString(R.string.test))
                    EditLocale( this) { recreate() }

                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name! (Running in )")
}

@Composable
fun ShowLocale(context: Context, text: String) {
    val locale = context.resources.configuration.locales[0]
    val language = locale.language
    val country = locale.country

    Text(text = "$text ($language-$country)")
}

@Composable
fun EditLocale(context: Context, callback: (config: Configuration) -> Unit = {}) {
    Button(onClick = {
        val currentLocale = Locale.getDefault()
        val newLocale = if (currentLocale.country == "NL") {
            Locale("en")
        } else {
            Locale("nl", "NL")
        }
        val config = context.resources.configuration;
        config.setLocale(newLocale)
        Locale.setDefault(newLocale)
        callback(config)
    }) {
        ShowLocale(context, context.getString(R.string.test))
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MobileDevelopment2_2Theme {
        Greeting("Android")
    }
}