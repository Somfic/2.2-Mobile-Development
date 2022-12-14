package com.example.mobile_development_2_2.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*

class Lang {
    companion object {
        private lateinit var context: Context
        private var locale: Locale = Locale.getDefault()

        private var languageCallbacks: ArrayList<() -> Unit> = ArrayList()
        private var colorblindCallbacks: ArrayList<() -> Unit> = ArrayList()

        val languages = arrayOf(Pair("English", "en"), Pair("Nederlands", "nl"))
        var language: Pair<String, String> by mutableStateOf(languages[0])

        var colorblind by mutableStateOf(false)

        fun setContext(context: Context) {
            if(!this::context.isInitialized) {
                this.context = context
                this.locale = context.resources.configuration.locale
                this.language = languages.find { it.second == locale.language } ?: languages[0]
            }
        }

        fun setLang(l: Pair<String, String>) {
            // Change the locale on the context
            context.resources.configuration.setLocale(Locale(l.second))
            context.resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)

            locale = Locale(l.second)
            language = l
            languageCallbacks.forEach { it() }
        }

        fun setColor(c: Boolean) {
            colorblind = c
            colorblindCallbacks.forEach { it() }
        }

        fun get(id: Int) : String {
            return context.resources.getString(id, locale)
        }

        fun onLanguageChanged(callback: () -> Unit) {
            languageCallbacks.add(callback)
        }

        fun onColorblindChange(callback: () -> Unit) {
            colorblindCallbacks.add(callback)
        }
    }
}