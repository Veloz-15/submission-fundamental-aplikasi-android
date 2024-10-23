package com.dicoding.submission1.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.dicoding.submission1.R
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.dicoding.submission1.data.response.EventResponse
import com.dicoding.submission1.data.response.ListEventsItem

class MainActivity : AppCompatActivity() {
    private lateinit var eventResponse: EventResponse
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var pref: SettingPreferences
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        pref = SettingPreferences.getInstance(dataStore)
        mainViewModel =
            ViewModelProvider(this, ViewModelFactory(pref)).get(MainViewModel::class.java)

        mainViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        loadingIndicator = findViewById(R.id.loadingIndicator)

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.navigation_upcoming -> {
                    loadFragment(UpcomingEvents())
                    true
                }

                R.id.navigation_finished -> {
                    loadFragment(FinishedEvents())
                    true
                }

                R.id.navigation_favorite -> {
                    loadFragment(FavoriteFragment())
                    true
                }

                R.id.navigation_setiings -> {
                    loadFragment(SettingApp())
                    true
                }

                else -> false
            }
        }

        if (savedInstanceState == null) {
            loadFragment(UpcomingEvents())
        }
    }

    fun setEventResponse(response: EventResponse) {
        eventResponse = response
    }

    private fun moveActivity(event: ListEventsItem) {
        val intent = Intent(this, DetailEvent::class.java).apply {
            putExtra(DetailEvent.EXTRA_EVENT_ID, event.id)
        }
        startActivity(intent)
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    fun setLoading(isLoading: Boolean) {
        loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun loadDataFromApi() {
        setLoading(true)
    }
}
