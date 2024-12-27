package com.example.androidmobile_sub02.ui.recipes

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.androidmobile_sub02.adapter.RecipeAdapter
import com.example.androidmobile_sub02.databinding.ActivitityResultScanBinding
import com.example.androidmobile_sub02.ui.recipes.RecipeViewModel

class RecipeActivity : AppCompatActivity() {

    private lateinit var binding: ActivitityResultScanBinding
    private lateinit var recipeAdapter: RecipeAdapter

    private val recipeViewModel: RecipeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitityResultScanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView for 2 columns
        setupRecyclerView()

        // Call getRecipes to load the data
        recipeViewModel.getRecipes()

        // Observe the LiveData from ViewModel
        recipeViewModel.listDataRecipes.observe(this, Observer { recipes ->
            if (recipes != null) {
                // Update RecyclerView with the new data
                recipeAdapter = RecipeAdapter(recipes, this)  // Pass context to adapter
                binding.rvEventFinished.adapter = recipeAdapter
            }
        })

        // Observe the loading state
        recipeViewModel.isLoading.observe(this, Observer { isLoading ->
            // Show or hide loading indicator based on isLoading
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setupRecyclerView() {
        // Set up RecyclerView with a GridLayoutManager (2 columns)
        binding.rvEventFinished.layoutManager = GridLayoutManager(this, 2)  // 2 items per row
    }
}
