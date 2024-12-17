package com.example.androidmobile_sub02.ui.upComingEvent

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmobile_sub02.R
import com.example.androidmobile_sub02.adapter.UpcomingEventAdapter
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.databinding.FragmentUpCommingEventBinding
import com.example.androidmobile_sub02.ui.detailEvent.DetailActivity

class UpComingEventFragment : Fragment() {

    private var _binding: FragmentUpCommingEventBinding? = null
    private val upComingEventViewModel by viewModels<UpComingEventViewModel>()
    private val binding get() = _binding!!
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUpCommingEventBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcomingEvent.layoutManager = layoutManager

        upComingEventViewModel.isLoading.observe(viewLifecycleOwner){
            showLoading(it)
        }

        upComingEventViewModel.listUpComingEvent.observe(viewLifecycleOwner){
            setEvent(it)
        }
    }

    private fun setEvent(it: List<ListEventsItem>){
        val adapter = UpcomingEventAdapter()
        adapter.submitList(it)
        binding.rvUpcomingEvent.adapter = adapter

        adapter.setOnItemClickCallback(object : UpcomingEventAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ListEventsItem) {
                selectedEvent(data)
            }
        })
    }

    private fun selectedEvent(data: ListEventsItem) {
        val intent = Intent(requireActivity(), DetailActivity::class.java)
        intent.putExtra(DetailActivity.ID_EVENT_KEY, data.id)
        startActivity(intent)
    }

    private fun showLoading(it: Boolean?) {
        binding.progressBar.visibility = if (it == true) View.VISIBLE else View.GONE
    }
}