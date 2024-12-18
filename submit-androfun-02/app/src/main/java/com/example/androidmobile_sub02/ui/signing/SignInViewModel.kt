package com.example.androidmobile_sub02.ui.signing

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class SignInViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {
    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    fun signIn(username: String, password: String) {
        viewModelScope.launch {
            // Basic validation logic
            if (username.isNotBlank() && password.length >= 6) {
                // Save the status of onboarding completion
                sharedPreferences.edit().putBoolean("isOnboardingCompleted", true).apply()
                _loginResult.value = LoginResult.Success
            } else {
                _loginResult.value = LoginResult.Error("Invalid credentials")
            }
        }
    }

    sealed class LoginResult {
        object Success : LoginResult()
        data class Error(val message: String) : LoginResult()
    }
}
