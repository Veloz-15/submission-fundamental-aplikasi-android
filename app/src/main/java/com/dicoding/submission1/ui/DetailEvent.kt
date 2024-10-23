package com.dicoding.submission1.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.submission1.R
import com.dicoding.submission1.data.response.ListEventsItem
import com.dicoding.submission1.database.FavEventDao
import com.dicoding.submission1.database.FavEventDatabase
import com.dicoding.submission1.database.FavoriteEvent
import kotlinx.coroutines.launch

class DetailEvent : AppCompatActivity() {

    companion object {
        const val EXTRA_EVENT_RESPONSE = "extraEventResponse"
        const val EXTRA_EVENT_ID = "extraEventId"
    }

    private lateinit var imgEventCover: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvBeginTime: TextView
    private lateinit var tvOwner: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvQuota: TextView
    private lateinit var button: Button
    private lateinit var loadingIndicator: LinearLayout
    private lateinit var icFavorite: ImageView
    private var isFavorited = false

    private lateinit var favEventDao: FavEventDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

        imgEventCover = findViewById(R.id.imgEventCover)
        tvTitle = findViewById(R.id.tvTitle)
        tvBeginTime = findViewById(R.id.tvBeginTime)
        tvOwner = findViewById(R.id.tvOwner)
        tvDescription = findViewById(R.id.tvDescription)
        tvQuota = findViewById(R.id.tvQuota)
        button = findViewById(R.id.button)
        icFavorite = findViewById(R.id.icFavorite)
        loadingIndicator = findViewById(R.id.loadingIndicator)

        favEventDao = FavEventDatabase.getDatabase(this).favEventDao()

        loadEventDetails()
    }

    private fun loadEventDetails() {
        loadingIndicator.visibility = View.VISIBLE

        val event = intent.getParcelableExtra<ListEventsItem>(EXTRA_EVENT_RESPONSE)

        if (event != null) {
            Glide.with(this).load(event.mediaCover).into(imgEventCover)
            tvTitle.text = event.name
            tvBeginTime.text = event.beginTime
            tvOwner.text = event.ownerName
            tvDescription.text = Html.fromHtml(event.description, Html.FROM_HTML_MODE_LEGACY)

            val remainingQuota = event.quota - event.registrants
            tvQuota.text = "Sisa kuota: ${if (remainingQuota < 0) 0 else remainingQuota}"

            loadingIndicator.visibility = View.GONE

            button.setOnClickListener {
                openWeb(event.link)
            }

            checkIfFavorited(event)

            icFavorite.setOnClickListener {
                toggleFavorite(event)
            }
        } else {
            tvTitle.text = "Event not found"
            loadingIndicator.visibility = View.GONE
        }
    }

    private fun openWeb(url: String?) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        startActivity(intent)
    }


    private fun checkIfFavorited(event: ListEventsItem) {
        lifecycleScope.launch {
            val favoriteEvent = favEventDao.getFavoriteById(event.id.toString())
            isFavorited = favoriteEvent != null
            updateFavoriteIcon()
        }
    }

    private fun toggleFavorite(event: ListEventsItem) {
        isFavorited = !isFavorited
        updateFavoriteIcon()
        handleFavorite(event)
    }

    private fun updateFavoriteIcon() {
        if (isFavorited) {
            icFavorite.setImageResource(R.drawable.baseline_favorite_24)
        } else {
            icFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
        }
    }

    private fun handleFavorite(event: ListEventsItem) {
        val favoriteEvent = FavoriteEvent(
            id = event.id.toString(),
            name = event.name,
            mediaCover = event.mediaCover,
            summary = event.summary,
            registrants = event.registrants,
            imageLogo = event.imageLogo,
            link = event.link,
            description = event.description,
            ownerName = event.ownerName,
            cityName = event.cityName,
            quota = event.quota,
            beginTime = event.beginTime,
            endTime = event.endTime,
            category = event.category
        )

        lifecycleScope.launch {
            try {
                if (isFavorited) {
                    favEventDao.insert(favoriteEvent)
                } else {
                    favEventDao.delete(favoriteEvent)
                }
            } catch (e: Exception) {
                Log.e("DetailEvent", "Error: ${e.message}")
            }
        }
    }
}
