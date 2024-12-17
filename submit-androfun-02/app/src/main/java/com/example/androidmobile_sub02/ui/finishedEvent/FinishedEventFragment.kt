package com.example.androidmobile_sub02.ui.finishedEvent

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmobile_sub02.adapter.FinishedEventAdapter
import com.example.androidmobile_sub02.adapter.UpComingHomeAdapter
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.data.remote.response.Recipe
import com.example.androidmobile_sub02.databinding.FragmentFinishedEventBinding
import com.example.androidmobile_sub02.ui.detailEvent.DetailActivity

class FinishedEventFragment : Fragment() {

    private var _binding: FragmentFinishedEventBinding? = null
    private val finishedEventViewModel by viewModels<FinishedEventViewModel>()
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedEventBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Finished Recipes RecyclerView
        val recipesLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvEventFinished.layoutManager = recipesLayoutManager

        // Upcoming Events RecyclerView
        val upcomingLayoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )
        binding.rvUpcomingEvent.layoutManager = upcomingLayoutManager

        // Observe loading state
        finishedEventViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        // Observe recipes
        finishedEventViewModel.listDataRecipes.observe(viewLifecycleOwner) { recipeList ->
            Log.e("TAG", "Recipes: $recipeList")
            setFinishedRecipes(recipeList)
        }

        // Observe upcoming events
        finishedEventViewModel.listDataRecipes.observe(viewLifecycleOwner) { recipeList ->
            setUpUpComingEvent(recipeList)
        }
    }

    private fun setUpUpComingEvent(recipeList: List<Recipe>) {
        val adapter = UpComingHomeAdapter()
        adapter.submitList(recipeList)
        binding.rvUpcomingEvent.adapter = adapter

        adapter.setOnItemClickCallback(object : UpComingHomeAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                // Open recipe source URL in browser
                data.source?.let { source ->
                    try {
                        val uri = Uri.parse(source)
                        val intent = Intent(Intent.ACTION_VIEW, uri)
                        startActivity(intent)
                    } catch (e: Exception) {
                        Log.e("FinishedEventFragment", "Error opening source URL: ${e.message}")
                    }
                }
            }
        })
    }

    private fun setFinishedRecipes(recipeList: List<Recipe>) {
        val adapter = FinishedEventAdapter()
        adapter.submitList(recipeList)
        binding.rvEventFinished.adapter = adapter

        adapter.setOnItemClickCallback(object : FinishedEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Recipe) {
                data.id?.let { recipeId ->
                    try {
                        val intent = Intent(context, DetailActivity::class.java)
                        intent.putExtra(DetailActivity.ID_EVENT_KEY, recipeId)
                        context?.startActivity(intent)
                    } catch (e: Exception){
                        Log.e("FinishedEventFragment", "Error redirection to Detail Recipe: ${e.message}")
                    }
                }
                // Open recipe source URL in browser
//                data.source?.let { source ->
//                    try {
//                        val uri = Uri.parse(source)
//                        val intent = Intent(Intent.ACTION_VIEW, uri)
//                        startActivity(intent)
//                    } catch (e: Exception) {
//                        Log.e("FinishedEventFragment", "Error opening source URL: ${e.message}")
//                    }
//                }
            }
        })
    }

    private fun selectedUpComingItem(data: Recipe) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.ID_EVENT_KEY, data.id)
        startActivity(intent)
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}