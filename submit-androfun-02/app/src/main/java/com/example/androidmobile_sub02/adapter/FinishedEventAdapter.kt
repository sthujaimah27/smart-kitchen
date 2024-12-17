package com.example.androidmobile_sub02.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidmobile_sub02.R
import com.example.androidmobile_sub02.data.remote.response.Recipe
import com.example.androidmobile_sub02.databinding.ItemListEventFinishedBinding

class FinishedEventAdapter : ListAdapter<Recipe, FinishedEventAdapter.MyViewHolder>(DIFF_CALLBACK) {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    class MyViewHolder(val binding: ItemListEventFinishedBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemListEventFinishedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val recipe = getItem(position)

        // Log the recipe object
//        Log.d("RecipeAdapter", "Recipe at position $position: $recipe")
//
//        // Set the recipe title
//        holder.binding.tvEventName.text = recipe.title ?: "Unnamed Recipe"
        holder.binding.tvEventName.text = if ((recipe.title?.length ?: 0) > 15) {
            // Truncate the title to 18 characters and append "..."
            recipe.title?.take(15) + "..."
        } else {
            // Use the full title or "Unnamed Recipe" if the title is null
            recipe.title ?: "Unnamed Recipe"
        }

//
//        // Load image with Glide
//         Note: You'll need to update this with the actual image URL from your Recipe object
        Glide.with(holder.itemView.context)
            .load(recipe.image) // Use the actual image URL
            .placeholder(R.drawable.empty_image) // Use a placeholder image
            .error(R.drawable.empty_image) // Use an error image if loading fails
            .centerCrop()
            .override(300, 250) // Set a fixed size
            .into(holder.binding.imgEvent)

        holder.binding.itemRoot.setOnClickListener {
            onItemClickCallback.onItemClicked(recipe)
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: Recipe)
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Recipe>() {
            override fun areItemsTheSame(
                oldItem: Recipe,
                newItem: Recipe
            ): Boolean {
                return oldItem.id == newItem.id
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