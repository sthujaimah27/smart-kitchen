package com.example.androidmobile_sub02.ui.themeSetting

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.androidmobile_sub02.R
import com.example.androidmobile_sub02.databinding.ActivityThemeSettingBinding
import com.example.androidmobile_sub02.ui.ViewModelFactory
import com.example.androidmobile_sub02.ui.splashScreen.SplashScreen
import kotlinx.coroutines.launch

class ThemeSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityThemeSettingBinding
    private val viewModel: ThemeSettingViewModel by viewModels() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.statusBarColor = ContextCompat.getColor(this, R.color.dicoding_color)
        supportActionBar?.title = "Settings"
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24)
        }

        binding = ActivityThemeSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        lifecycleScope.launch {
             viewModel.getThemeSetting().collect{ isDarkModeActive ->
                 binding.switchTheme.isChecked = isDarkModeActive
                 setAppTheme(isDarkModeActive)
             }
        }

        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemeSetting(isChecked)
            val intent = Intent(this@ThemeSettingActivity, SplashScreen::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun setAppTheme(darkModeActive: Boolean) {
        if (darkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}