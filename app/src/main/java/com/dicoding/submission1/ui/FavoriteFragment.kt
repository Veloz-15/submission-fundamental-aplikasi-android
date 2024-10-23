package com.dicoding.submission1.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.submission1.R
import com.dicoding.submission1.data.response.ListEventsItem
import com.dicoding.submission1.database.FavEventDao
import com.dicoding.submission1.database.FavEventDatabase

class FavoriteFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private lateinit var favEventDao: FavEventDao
    private lateinit var loadingIndicator: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_favorite, container, false)

        recyclerView = view.findViewById(R.id.rvFavorite)
        eventAdapter = EventAdapter { event ->
            val intent = Intent(requireContext(), DetailEvent::class.java).apply {
                putExtra(DetailEvent.EXTRA_EVENT_RESPONSE, event)
            }
            startActivity(intent)
        }

        recyclerView.adapter = eventAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        loadingIndicator = inflater.inflate(R.layout.partial_loading, container, false)

        (view as? ViewGroup)?.addView(loadingIndicator)

        val favEventDatabase = FavEventDatabase.getDatabase(requireContext())
        favEventDao = favEventDatabase.favEventDao()

        loadFavorites()

        return view
    }

    private fun loadFavorites() {
        loadingIndicator.visibility = View.VISIBLE

        favEventDao.getAllFavorites().observe(viewLifecycleOwner) { favoriteEvents ->
            loadingIndicator.visibility = View.GONE
            val items = ArrayList<ListEventsItem>()
            favoriteEvents.forEach { favEvent ->
                val item = ListEventsItem(
                    id = favEvent.id.toInt(),
                    name = favEvent.name,
                    mediaCover = favEvent.mediaCover ?: "",
                    summary = favEvent.summary,
                    registrants = favEvent.registrants,
                    imageLogo = favEvent.imageLogo ?: "",
                    link = favEvent.link,
                    description = favEvent.description,
                    ownerName = favEvent.ownerName,
                    cityName = favEvent.cityName,
                    quota = favEvent.quota,
                    beginTime = favEvent.beginTime,
                    endTime = favEvent.endTime,
                    category = favEvent.category
                )
                items.add(item)
            }
            eventAdapter.setEvents(items)
        }
    }
}
