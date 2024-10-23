package com.dicoding.submission1.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.submission1.R
import com.dicoding.submission1.data.response.EventResponse
import com.dicoding.submission1.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingEvents : Fragment() {
    private lateinit var rvUpcomingEvents: RecyclerView
    private lateinit var eventAdapter: EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_upcoming_events, container, false)
        rvUpcomingEvents = view.findViewById(R.id.rvUpcoming)
        initRecyclerView()
        getUpcomingEvents()
        return view
    }

    private fun initRecyclerView() {
        eventAdapter = EventAdapter { event ->
            Log.d("UpcomingEvents", "Sending Event ID: ${event.id}")
            val intent = Intent(requireContext(), DetailEvent::class.java).apply {
                putExtra(DetailEvent.EXTRA_EVENT_ID, event.id)
            }
            startActivity(intent)
        }

        rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext())
        rvUpcomingEvents.adapter = eventAdapter
    }

    private fun getUpcomingEvents() {
        (activity as? MainActivity)?.setLoading(true)

        ApiConfig.getApiService().getUpcomingEvents().enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                (activity as? MainActivity)?.setLoading(false)

                if (response.isSuccessful) {
                    val eventResponse = response.body()
                    eventResponse?.let {
                        (activity as? MainActivity)?.setEventResponse(it)
                        eventAdapter.setEvents(it.listEvents.toMutableList())
                    }
                } else {
                    Log.e(
                        "UpcomingEventsFragment",
                        "Response error: ${response.errorBody()?.string()}"
                    )
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                (activity as? MainActivity)?.setLoading(false)
                Log.e("UpcomingEventsFragment", "Error: ${t.message}")
            }
        })
    }
}
