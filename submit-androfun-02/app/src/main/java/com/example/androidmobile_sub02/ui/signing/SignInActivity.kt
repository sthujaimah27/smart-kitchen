package com.example.androidmobile_sub02.ui.signing

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout
import android.widget.TextView
import com.example.androidmobile_sub02.MainActivity
import com.example.androidmobile_sub02.R

class SignInActivity : AppCompatActivity() {
    private val viewModel: SignInViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)

        val emailLayout = findViewById<TextInputLayout>(R.id.usernameLayout)
        val passwordLayout = findViewById<TextInputLayout>(R.id.passwordLayout)
        val emailEt = findViewById<TextInputEditText>(R.id.usernameEt)
        val passEt = findViewById<TextInputEditText>(R.id.passET)
        val signInButton = findViewById<MaterialButton>(R.id.btnSignIn)
        val signUpText = findViewById<TextView>(R.id.textView)

        // Observe login result
        viewModel.loginResult.observe(this) { result ->
            when (result) {
                is SignInViewModel.LoginResult.Success -> {
                    // Navigate to MainActivity
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                is SignInViewModel.LoginResult.Error -> {
                    // Show error
                    Toast.makeText(this, result.message, Toast.LENGTH_SHORT).show()
                }
            }
        }

        signInButton.setOnClickListener {
            val email = emailEt.text.toString()
            val password = passEt.text.toString()

            // Validate and attempt sign in
            viewModel.signIn(email, password)
        }

        signUpText.setOnClickListener {
            // Placeholder for sign up navigation
            Toast.makeText(this, "Sign Up functionality to be implemented", Toast.LENGTH_SHORT).show()
        }
    }
}