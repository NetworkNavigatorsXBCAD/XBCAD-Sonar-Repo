package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class AdminLoginActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private var isPasswordVisible = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        auth = FirebaseAuth.getInstance()

        val emailEditText: EditText = findViewById(R.id.adminEmailInput)
        val passwordEditText: EditText = findViewById(R.id.adminPasswordInput)
        val loginButton: Button = findViewById(R.id.loginButton)
        val registerButton: Button = findViewById(R.id.registerButton)
        val passwordToggle: ImageButton = findViewById(R.id.adminPasswordToggle)

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
            passwordEditText.setSelection(passwordEditText.text.length)  // Keep cursor at end
        }

        // Login button click listener
        loginButton.setOnClickListener {
            val email = emailEditText.text.toString()
            val password = passwordEditText.text.toString()
            loginUser(email, password)
        }

        // Register button can navigate to registration page (optional)
        registerButton.setOnClickListener {
            val intent = Intent(this, AdminRegistrationActivity::class.java)
            startActivity(intent)
        }
    }

    /*This code was adapted from StackOverflow
 https://stackoverflow.com/questions/52863176/firebase-user-registration-issue
 Author - Peter Haddad
 https://stackoverflow.com/users/7015400/peter-haddad
  */


    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to the admin home page
                    val intent = Intent(this, AdminActivity::class.java)
                    startActivity(intent)
                    finish()  // Close the login activity
                } else {
                    // Handle login failure
                    Toast.makeText(
                        this,
                        "Login failed: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }
}
