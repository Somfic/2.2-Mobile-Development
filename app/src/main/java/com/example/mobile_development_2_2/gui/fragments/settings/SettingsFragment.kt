package com.example.mobile_development_2_2.gui.fragments.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.mobile_development_2_2.R

class SettingsFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}