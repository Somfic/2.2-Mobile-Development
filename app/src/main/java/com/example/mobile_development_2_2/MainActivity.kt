package com.example.mobile_development_2_2

import android.graphics.Paint.Align
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.text.font.FontWeight.Companion.Black
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_development_2_2.ui.theme.MobileDevelopment2_2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApp {
                MyScreenContent()
            }
        }
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit){
    MobileDevelopment2_2Theme {
        // A surface container using the 'background' color from the theme
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colors.background
        ) {
            content();

        }
    }
}

@Composable
fun MyScreenContent(names: List<String> = listOf("Android", "There"), buttonNames: List<String> = listOf("Home, Route, POI")){
    Column {
        for(name: String in names){
            Greeting(name = name);
            Divider();
        }
        Counter();

    }

    Row() {
        Modifier.align
        for (buttonName: String in buttonNames) {
            Button(onClick = { }) {
                Text(text = buttonName)
            }
        }
    }
}

@Composable
fun Counter(){
    var counter by remember {
        mutableStateOf(0 )
    }
    Button(onClick = { counter++ }) {
        Text(text = "I've been clicked $counter times")
    }
}

@Composable
fun Greeting(name: String) {
        Text(
            text = "Hello $name!",
            modifier = Modifier.padding(16.dp)
        )
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApp {
        MyScreenContent()
    }
}