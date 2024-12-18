package com.example.androidmobile_sub02

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton
import com.example.androidmobile_sub02.databinding.ActivityMainBinding
import com.example.androidmobile_sub02.databinding.ActivitySigninBinding
import com.example.androidmobile_sub02.ui.ViewModelFactory
import com.example.androidmobile_sub02.ui.favEvent.FavoriteEventActivity
import com.example.androidmobile_sub02.ui.themeSetting.ThemeSettingActivity
import com.example.androidmobile_sub02.ui.themeSetting.ThemeSettingViewModel
import com.example.androidmobile_sub02.adapter.OnBoardingAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var signInBinding: ActivitySigninBinding
    private val viewModel: ThemeSettingViewModel by viewModels {
        ViewModelFactory.getInstance(application)
    }

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()
        window.statusBarColor = ContextCompat.getColor(this, R.color.dicoding_color)

        sharedPreferences = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        val isOnboardingCompleted = sharedPreferences.getBoolean("isOnboardingCompleted", false)
        val isLogin = sharedPreferences.getBoolean("isLogin", false)

        // Navigate based on the onboarding and login status
        when {
            !isOnboardingCompleted -> {
                setContentView(R.layout.activity_onboarding)
                setupOnboardingScreen()
            }
            !isLogin -> {
                setContentView(R.layout.activity_signin) // Load the sign-in layout
                setupSignInScreen()
            }
            else -> {
                setupMainScreen()
            }
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
                sharedPreferences.edit().putBoolean("isOnboardingCompleted", true).apply()
                val isLogin = sharedPreferences.getBoolean("isLogin", false)
                if (isLogin) {
                    setupMainScreen()
                } else {
                    setContentView(R.layout.activity_signin)
                    setupSignInScreen()
                }
            }
        }
    }

    private fun setupSignInScreen() {
        signInBinding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(signInBinding.root)

        signInBinding.btnSignIn.setOnClickListener {
            val username = signInBinding.usernameEt.text.toString()
            val password = signInBinding.passET.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                // Make the network request to log in
                loginRequest(username, password)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginRequest(username: String, password: String) {
        val client = OkHttpClient()
        val mediaType = "application/x-www-form-urlencoded".toMediaType()
        val body = "username=$username&password=$password".toRequestBody(mediaType)
        val request = Request.Builder()
            .url("http://34.101.181.114:3000/login")
            .post(body)
            .addHeader("Content-Type", "application/x-www-form-urlencoded")
            .build()

        lifecycleScope.launch {
            try {
                val response = withContext(Dispatchers.IO) {
                    client.newCall(request).execute()
                }

                if (response.isSuccessful) {
                    // Parse the JSON response
                    val responseData = response.body?.string()
                    Log.d("LoginResponse", "Response: $responseData")

                    // Check the status in the response
                    val jsonResponse = responseData?.let { JSONObject(it) }
                    val status = jsonResponse?.optString("status")

                    if (status == "fail") {
                        // If the status is "fail", get the message and show an error
                        val errorMessage = jsonResponse.optString("message", "Unknown error")
                        Log.e("LoginError", "Error: $errorMessage")
                        runOnUiThread {
                            Toast.makeText(applicationContext, "Login failed: $errorMessage", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        // Handle successful login (status is not "fail")
                        sharedPreferences.edit().putBoolean("isLogin", true).apply()
                        setupMainScreen()
                    }
                } else {
                    // Handle HTTP error response (non-2xx)
                    runOnUiThread {
                        Toast.makeText(applicationContext, "Login failed: ${response.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                Log.e("LoginError", "Exception: ${e.message}")
                runOnUiThread {
                    Toast.makeText(applicationContext, "Network error. Please try again later.", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private fun setupNavigation() {
        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.navigation_finished, R.id.navigation_upcoming, R.id.navigation_home)
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
        when (item.itemId) {
            R.id.setting_menu -> {
                val intent = Intent(this@MainActivity, ThemeSettingActivity::class.java)
                startActivity(intent)
            }
            R.id.favorite_menu -> {
                val intent = Intent(this@MainActivity, FavoriteEventActivity::class.java)
                startActivity(intent)
            }
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }
}
