package com.example.androidmobile_sub02.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.androidmobile_sub02.R
import com.example.androidmobile_sub02.data.remote.response.Recipe
import com.example.androidmobile_sub02.ui.detailEvent.DetailActivity

class RecipeAdapter(private val recipes: List<Recipe>, private val context: Context?) : RecyclerView.Adapter<RecipeAdapter.RecipeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecipeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_list_event_finished, parent, false)
        return RecipeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecipeViewHolder, position: Int) {
        val recipe = recipes[position]

        // Set recipe title
        holder.tvEventName.text = recipe.title

        // Load image with Glide
        Glide.with(holder.itemView.context)
            .load(recipe.image) // Load image from the URL
            .into(holder.imgEvent)

        // Set item click listener
        holder.itemView.setOnClickListener {
            try {
                val intent = Intent(context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.ID_EVENT_KEY, recipe.id)
                context?.startActivity(intent)
            } catch (e: Exception) {
                Log.e("RecipeAdapter", "Error redirection to Detail Recipe: ${e.message}")
            }
        }
    }

    override fun getItemCount(): Int = recipes.size

    class RecipeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvEventName: TextView = itemView.findViewById(R.id.tv_event_name)
        val imgEvent: ImageView = itemView.findViewById(R.id.img_event)
    }
}
