package com.example.mobile_development_2_2.data

import android.content.Context
import java.util.*


class Lang {
    companion object {
        private lateinit var context: Context

        fun setContext(context: Context) {
            this.context = context
        }

        fun setLanguage(language: String) {
            Locale.setDefault(Locale(language))
        }

        fun get(id: Int) : String {
            return context.resources.getString(id, Locale.getDefault())
        }
    }
}