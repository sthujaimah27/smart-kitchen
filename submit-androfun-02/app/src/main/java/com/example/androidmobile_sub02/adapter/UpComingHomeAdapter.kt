package com.example.androidmobile_sub02.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.data.remote.response.Recipe
import com.example.androidmobile_sub02.databinding.ItemUpcomingHomeBinding

class UpComingHomeAdapter: ListAdapter<Recipe, UpComingHomeAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemUpcomingHomeBinding): RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUpcomingHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val dataEvent = getItem(position)
//        holder.binding.tvEventName.text = dataEvent.name
        Glide.with(holder.itemView.context)
            .load(dataEvent.image)
            .centerCrop()
            .into(holder.binding.imgEvent)
        holder.binding.itemRoot.setOnClickListener {
            onItemClickCallback.onItemClicked(dataEvent)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Recipe)
    }

    companion object{
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(
                oldItem: Recipe,
                newItem: Recipe
            ): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: Recipe,
                newItem: Recipe
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}