package com.example.androidmobile_sub02.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidmobile_sub02.adapter.FinishedEventHomeAdapter.OnItemClickCallback
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.databinding.ItemListEventFinishedBinding
import com.example.androidmobile_sub02.databinding.ItemListEventUpcomingBinding
import com.example.androidmobile_sub02.utils.DateUtils

class UpcomingEventAdapter: ListAdapter<ListEventsItem, UpcomingEventAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemListEventUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListEventUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataEvent = getItem(position)
        val date = DateUtils.formatDate(dataEvent.beginTime?: "")
        Glide.with(holder.itemView.context)
            .load(dataEvent.mediaCover)
            .centerCrop()
            .into(holder.binding.imgEvent)

        holder.binding.tvEventName.text = dataEvent.name
        holder.binding.tvEventDate.text = date ?: ""
        holder.binding.tvQuota.text = "${dataEvent.registrants} / ${dataEvent.quota} Quotas"
        holder.binding.itemRoot.setOnClickListener {
            onItemClickCallback.onItemClicked(dataEvent)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: ListEventsItem)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}