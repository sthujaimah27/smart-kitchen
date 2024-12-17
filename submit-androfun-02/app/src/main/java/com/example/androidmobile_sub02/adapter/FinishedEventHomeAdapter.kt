package com.example.androidmobile_sub02.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidmobile_sub02.adapter.FinishedEventAdapter.OnItemClickCallback
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.databinding.ItemFinishedHomeBinding

class FinishedEventHomeAdapter: ListAdapter<ListEventsItem, FinishedEventHomeAdapter.ViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class ViewHolder(val binding: ItemFinishedHomeBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFinishedHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataEvent = getItem(position)
        holder.binding.tvEventName.text = dataEvent.name
        Glide.with(holder.itemView.context)
            .load(dataEvent.imageLogo)
            .centerCrop()
            .into(holder.binding.imageEvent)
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