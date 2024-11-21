package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class CustomerRegistrationActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false
    private var isConfirmPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_registration)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.customerEmailInput)
        val passwordEditText: EditText = findViewById(R.id.customerPasswordInput)
        val confirmPasswordEditText: EditText = findViewById(R.id.customerConfirmPasswordInput)
        val registerButton: Button = findViewById(R.id.customerRegisterButton)
        val loginButton: Button = findViewById(R.id.customerLoginButton)
        val passwordToggle: ImageButton = findViewById(R.id.passwordToggle)
        val confirmPasswordToggle: ImageButton = findViewById(R.id.confirmPasswordToggle)
        val backButton: ImageButton = findViewById(R.id.backButton)

        // Back button click to navigate to MainActivity
        backButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Toggle password visibility
        passwordToggle.setOnClickListener {
            isPasswordVisible = !isPasswordVisible
            if (isPasswordVisible) {
                passwordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                passwordToggle.setImageResource(R.drawable.eyeopen)  // Switch to eye open
            } else {
                passwordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                passwordToggle.setImageResource(R.drawable.eyeicon)  // Switch to eye closed
            }
            passwordEditText.setSelection(passwordEditText.text.length) // Move cursor to end
        }

        // Toggle confirm password visibility
        confirmPasswordToggle.setOnClickListener {
            isConfirmPasswordVisible = !isConfirmPasswordVisible
            if (isConfirmPasswordVisible) {
                confirmPasswordEditText.inputType = InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
                confirmPasswordToggle.setImageResource(R.drawable.eyeopen)  // Switch to eye open
            } else {
                confirmPasswordEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
                confirmPasswordToggle.setImageResource(R.drawable.eyeicon)  // Switch to eye closed
            }
            confirmPasswordEditText.setSelection(confirmPasswordEditText.text.length) // Move cursor to end
        }

        // Register button click
        registerButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            if (password == confirmPassword) {
                registerUser(email, password)
            } else {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
            }
        }

        // Login button click
        loginButton.setOnClickListener {
            // Navigate to the login activity
            val intent = Intent(this, CustomerLoginActivity::class.java)
            startActivity(intent)
        }
    }

    /*This code was adapted from StackOverflow
    https://stackoverflow.com/questions/52863176/firebase-user-registration-issue
    Author - Peter Haddad
    https://stackoverflow.com/users/7015400/peter-haddad
     */

    private fun registerUser(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Registration successful, navigate to the login page
                    val intent = Intent(this, CustomerLoginActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Handle registration failure
                    Toast.makeText(
                        this,
                        "Registration failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}