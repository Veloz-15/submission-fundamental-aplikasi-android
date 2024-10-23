package com.dicoding.submission1.data.retrofit

import com.dicoding.submission1.data.response.EventResponse
import com.dicoding.submission1.data.response.ListEventsItem
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("events/{id}")
    fun getDetailEvent(@Path("id") id: String): Call<ListEventsItem>

    @GET("events")
    fun getEventById(@Query("active") active: Int): Call<EventResponse>

    @GET("events?active=1")
    fun getUpcomingEvents(): Call<EventResponse> {
        return getEventById(1)
    }

    @GET("events?active=0")
    fun getFinishedEvents(): Call<EventResponse> {
        return getEventById(0)
    }
}