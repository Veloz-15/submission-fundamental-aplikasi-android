package com.dicoding.submission1.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.submission1.data.response.ListEventsItem
import com.dicoding.submission1.databinding.ListEventBinding

class EventAdapter(
    private val listener: (ListEventsItem) -> Unit
) : RecyclerView.Adapter<EventAdapter.ViewHolder>() {

    private var events: MutableList<ListEventsItem> = mutableListOf()

    fun setEvents(events: MutableList<ListEventsItem>) {
        this.events = events
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ListEventBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val event = events[position]
        holder.bind(event)

        holder.itemView.setOnClickListener {
            val intentDetail = Intent(holder.itemView.context, DetailEvent::class.java).apply {
                putExtra(DetailEvent.EXTRA_EVENT_RESPONSE, event)
            }
            holder.itemView.context.startActivity(intentDetail)
        }
    }

    override fun getItemCount(): Int {
        return events.size
    }

    inner class ViewHolder(val binding: ListEventBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem) {
            Glide.with(binding.imgPhoto.context)
                .load(event.mediaCover)
                .into(binding.imgPhoto)
            binding.tvTitle.text = event.name
        }
    }
}
