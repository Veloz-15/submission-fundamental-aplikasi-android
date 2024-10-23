package com.dicoding.submission1.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submission1.R
import com.google.android.material.switchmaterial.SwitchMaterial

class SettingApp : Fragment() {

    private lateinit var switchTheme: SwitchMaterial

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_setting_app, container, false)

        switchTheme = view.findViewById(R.id.switch1)

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val mainViewModel =
            ViewModelProvider(this, ViewModelFactory(pref)).get(MainViewModel::class.java)

        mainViewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            switchTheme.isChecked = isDarkModeActive
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            mainViewModel.saveThemeSetting(isChecked)
        }

        return view
    }
}

