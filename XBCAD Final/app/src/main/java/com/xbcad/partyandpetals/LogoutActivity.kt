package com.xbcad.partyandpetals

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.databinding.ActivityLogoutBinding

class LogoutActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLogoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogoutBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.pageTitle.text = "Logout"
    }
}



