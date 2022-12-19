package com.example.mobile_development_2_2.gui.fragments.settings

import android.os.Bundle
import android.widget.ToggleButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.DefaultAlpha
import androidx.preference.PreferenceFragmentCompat
import com.example.mobile_development_2_2.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.state.ToggleableState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.PopupProperties

class SettingsFragment : PreferenceFragmentCompat() {

    val languages = arrayOf("English", "Nederlands")

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }

    @Preview
    @Composable
    fun SettingsFragment() {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Settings()
            AgsLogo()
            Copyright()
        }
    }


    @Composable
    fun Settings() {
        var expanded by remember { mutableStateOf(false)}
        var language by remember {mutableStateOf(languages[0])}

        Column() {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = /* getString(R.string.settings_language) */ "Language",
                    modifier = Modifier.padding(8.dp),
                )
                Row(Modifier.clickable { expanded = !expanded }, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = language) // City name label
                    Icon(imageVector = Icons.Filled.ArrowDropDown, contentDescription = null)
                    DropdownMenu(
                        expanded = expanded,
                        onDismissRequest = { expanded = false },
                        properties = PopupProperties(focusable = false)
                    ) {
                        languages.forEach { l ->
                            DropdownMenuItem(onClick = {
                                expanded = false
                                language = l
                            }) {
                                Text(text = l)
                            }
                        }
                    }
                }
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = /*getString(R.string.settings_colour_blind)*/ "Color blind",
                    modifier = Modifier.padding(8.dp),
                )
                val checkedState = remember { mutableStateOf(true) }
                Switch(
                    checked = checkedState.value,
                    onCheckedChange = {
                        checkedState.value = it;

                                      },
                )
            }
        }
    }

    @Composable
    fun AgsLogo() {
        Image(
            painter = painterResource(id =R.drawable.ic_logo),
            contentDescription = null,
            modifier = Modifier
                //.align(Alignment.CenterStart)
                .clip(
                    RoundedCornerShape(24.dp)
                ),
            alignment = Alignment.Center,
            alpha = DefaultAlpha,
            colorFilter = null)
    }

    @Composable
    fun Copyright() {
        Text(
            text = /* getString(R.string.settings_copyright) */ "Made by A4",
            modifier = Modifier.padding(8.dp),
        )
    }
}