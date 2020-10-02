package com.example.rentalapp.ui.user

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import com.example.rentalapp.R

class UserFragment : PreferenceFragmentCompat() {

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}