package com.example.androidmobile_sub02.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.androidmobile_sub02.R

class OnBoardingAdapter (private val context: Context, private val onboardingItems: List<Int>) : RecyclerView.Adapter<OnBoardingAdapter.OnboardingViewHolder>() {
    inner class OnboardingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.ivOnboardingImage)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.onboarding_item, parent, false)
        return OnboardingViewHolder(view)
    }

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        val imageResource = onboardingItems[position]
        holder.imageView.setImageResource(imageResource)
    }

    override fun getItemCount(): Int {
        return onboardingItems.size
    }
}