package com.example.androidmobile_sub02

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.example.androidmobile_sub02.databinding.ActivityMainBinding
import com.example.androidmobile_sub02.ui.ViewModelFactory
import com.example.androidmobile_sub02.ui.favEvent.FavoriteEventActivity
import com.example.androidmobile_sub02.ui.themeSetting.ThemeSettingActivity
import com.example.androidmobile_sub02.ui.themeSetting.ThemeSettingViewModel
import com.example.androidmobile_sub02.adapter.OnBoardingAdapter
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: ThemeSettingViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        supportActionBar?.hide()
        super.onCreate(savedInstanceState)

        window.statusBarColor = ContextCompat.getColor(this, R.color.dicoding_color)

        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val isOnboardingCompleted = sharedPreferences.getBoolean("isOnboardingCompleted", false)

        if (!isOnboardingCompleted) {
            setContentView(R.layout.activity_onboarding)
            setupOnboardingScreen()
        } else {
            setupMainScreen()
        }

        lifecycleScope.launch {
            viewModel.getThemeSetting().collect { isDarkModeActive ->
                setAppTheme(isDarkModeActive)
            }
        }
    }

    private fun setupMainScreen() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupNavigation()
    }

    private fun setupOnboardingScreen() {
        supportActionBar?.hide()
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        val btnNext = findViewById<MaterialButton>(R.id.btnNext)

        val onboardingImages = listOf(R.drawable.ttr1, R.drawable.ttr2, R.drawable.ttr3)
        val onboardingAdapter = OnBoardingAdapter(this, onboardingImages)
        viewPager.adapter = onboardingAdapter

        btnNext.setOnClickListener {
            val currentPosition = viewPager.currentItem
            val nextPosition = currentPosition + 1
            if (nextPosition < onboardingAdapter.itemCount) {
                viewPager.currentItem = nextPosition
            } else {
                // Mark onboarding as completed
                sharedPreferences.edit().putBoolean("isOnboardingCompleted", true).apply()

                // Restart activity to load main screen
                recreate()
            }
        }
    }

    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_finished, R.id.navigation_upcoming, R.id.navigation_home,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun setAppTheme(darkModeActive: Boolean) {
        if (darkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.theme_app_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.setting_menu -> {
                val intent = Intent(this@MainActivity, ThemeSettingActivity::class.java)
                startActivity(intent)
            }
            R.id.favorite_menu -> {
                val intent = Intent(this@MainActivity, FavoriteEventActivity::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}