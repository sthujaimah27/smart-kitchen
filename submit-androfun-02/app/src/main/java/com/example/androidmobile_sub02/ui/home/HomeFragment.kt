package com.example.androidmobile_sub02.ui.home

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmobile_sub02.MainActivity
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
    private lateinit var sharedPreferences: SharedPreferences

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        sharedPreferences = requireContext().getSharedPreferences("my_prefs", Context.MODE_PRIVATE)

        // Set up the button click listener for "Keluar Akun"
        binding.root.findViewById<Button>(R.id.actionButton).setOnClickListener {
            // Set "isLogin" to false
            sharedPreferences.edit().putBoolean("isLogin", false).apply()

            // Restart the app by launching MainActivity and finishing the current activity
            val intent = Intent(requireContext(), MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            requireActivity().finish() // Close the current fragment's activity
        }

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
