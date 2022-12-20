package com.example.mobile_development_2_2.data

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import java.util.*

class Lang {
    companion object {
        private lateinit var context: Context
        private var locale: Locale = Locale.getDefault()

        private var callbacks: ArrayList<() -> Unit> = ArrayList()

        val languages = arrayOf(Pair("English", "en"), Pair("Nederlands", "nl"))
        var language: Pair<String, String> by mutableStateOf(languages[0])

        fun setContext(context: Context) {
            if(!this::context.isInitialized) {
                this.context = context
                this.locale = context.resources.configuration.locale
            }
        }

        fun set(l: Pair<String, String>) {
            // Change the locale on the context
            context.resources.configuration.setLocale(Locale(l.second))
            context.resources.updateConfiguration(context.resources.configuration, context.resources.displayMetrics)

            locale = Locale(l.second)
            language = l
            callbacks.forEach { it() }
        }

        fun get(id: Int) : String {
            return context.resources.getString(id, locale)
        }

        fun onLanguageChanged(callback: () -> Unit) {
            callbacks.add(callback)
        }
    }
}