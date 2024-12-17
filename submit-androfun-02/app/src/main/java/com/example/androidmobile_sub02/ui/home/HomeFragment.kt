package com.example.androidmobile_sub02.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmobile_sub02.R
import com.example.androidmobile_sub02.adapter.FinishedEventAdapter
import com.example.androidmobile_sub02.adapter.FinishedEventHomeAdapter
import com.example.androidmobile_sub02.adapter.UpComingHomeAdapter
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.databinding.FragmentHomeBinding
import com.example.androidmobile_sub02.ui.ViewModelFactory
import com.example.androidmobile_sub02.ui.detailEvent.DetailActivity
import com.example.androidmobile_sub02.ui.themeSetting.ThemeSettingViewModel
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel by viewModels<HomeViewModel>()
//    private val viewModel: ThemeSettingViewModel by viewModels() {
//        ViewModelFactory.getInstance(requireContext())
//    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}