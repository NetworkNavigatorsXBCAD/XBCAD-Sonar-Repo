package com.xbcad.partyandpetals

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.StockCategories.AddNewProductActivity
import com.xbcad.partyandpetals.StockCategories.AdminBubbleBalloonsActivity
import com.xbcad.partyandpetals.StockCategories.AdminFoilBalloonsActivity
import com.xbcad.partyandpetals.StockCategories.AdminLatexBalloonsActivity
import com.xbcad.partyandpetals.databinding.ActivityStockBinding

class StockActivity: AppCompatActivity() {
    private lateinit var binding: ActivityStockBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityStockBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBubble.setOnClickListener {
            startActivity(Intent(this, AdminBubbleBalloonsActivity::class.java))
        }

        binding.btnFoil.setOnClickListener {
            startActivity(Intent(this, AdminFoilBalloonsActivity::class.java))
        }

        binding.btnLatex.setOnClickListener {
            startActivity(Intent(this, AdminLatexBalloonsActivity::class.java))
        }

        binding.btnAddNewProduct.setOnClickListener {
            startActivity(Intent(this, AddNewProductActivity::class.java))
        }


        binding.bottomAdminNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_stock -> true // Handle the Current Orders selection
                R.id.nav_currentOrders -> {
                    startActivity(Intent(this, AdminActivity::class.java))
                    true
                }

                R.id.nav_LogOut -> {
                    logout()
                    true
                }

                else -> false
            }
        }
    }

    private fun logout() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
