package com.example.androidmobile_sub02.ui.favEvent

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.androidmobile_sub02.R
import com.example.androidmobile_sub02.adapter.UpcomingEventAdapter
import com.example.androidmobile_sub02.data.remote.response.ListEventsItem
import com.example.androidmobile_sub02.databinding.ActivityFavoriteEventBinding
import com.example.androidmobile_sub02.ui.ViewModelFactory
import com.example.androidmobile_sub02.ui.detailEvent.DetailActivity
import com.example.androidmobile_sub02.ui.themeSetting.ThemeSettingViewModel
import kotlinx.coroutines.launch

class FavoriteEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteEventBinding
    private val favEventViewModel by viewModels<FavoriteEventViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    private val viewModel: ThemeSettingViewModel by viewModels() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityFavoriteEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dicoding_color)
        supportActionBar?.title = "Your Favorite Event"
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val layoutManager = LinearLayoutManager(this)
        binding.rvFavEvent.layoutManager = layoutManager

        favEventViewModel.getAllFavEvent().observe(this){
            val adapter = UpcomingEventAdapter()
            val listItemEvent = it.map { eventEntity ->
                ListEventsItem(
                    id = eventEntity.id,
                    name = eventEntity.name,
                    beginTime = eventEntity.beginTime,
                    registrants = eventEntity.registrants,
                    quota = eventEntity.quota,
                    imageLogo = eventEntity.imageLogo,
                    description = eventEntity.description,
                    ownerName = eventEntity.ownerName,
                    cityName = eventEntity.cityName,
                    link = eventEntity.link,
                    summary = eventEntity.summary,
                    mediaCover = eventEntity.mediaCover,
                    category = eventEntity.category,
                    endTime = eventEntity.endTime,
                )
            }
            adapter.submitList(listItemEvent)
            binding.rvFavEvent.adapter = adapter

            adapter.setOnItemClickCallback(object : UpcomingEventAdapter.OnItemClickCallback{
                override fun onItemClicked(data: ListEventsItem) {
                    val intent = Intent(this@FavoriteEventActivity, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.ID_EVENT_KEY, data.id)
                    startActivity(intent)
                }
            })
        }

        favEventViewModel.isLoading.observe(this){
            showLoading(it)
        }

        lifecycleScope.launch {
            viewModel.getThemeSetting().collect{ isDarkModeActive ->
                setAppTheme(isDarkModeActive)
            }
        }
    }

    private fun setAppTheme(darkModeActive: Boolean) {
        if (darkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun showLoading(it: Boolean) {
        binding.progressBar.visibility = if (it == true) View.VISIBLE else View.GONE
        binding.rvFavEvent.visibility = if (it == true) View.GONE else View.VISIBLE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}