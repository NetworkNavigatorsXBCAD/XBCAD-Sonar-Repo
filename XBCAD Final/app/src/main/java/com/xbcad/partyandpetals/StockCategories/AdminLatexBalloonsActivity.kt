package com.xbcad.partyandpetals.StockCategories
//this code was adapted from stackoverflow
//https://stackoverflow.com/questions/75720964/view-binding-to-switch-between-activities
//Muhammad Asad
//https://stackoverflow.com/users/13903584/muhammad-asad

//this code was adapted from stackoverflow
//https://stackoverflow.com/questions/77481039/android-studio-kotlin-navigation-drawer-bottom-navigation-bar
//UwUs
//https://stackoverflow.com/users/19108801/uwus

//this code was adapted from stackoverflow
//https://stackoverflow.com/questions/73510902/how-to-observe-a-list-from-adapter-in-android-kotlin
//oyeraghib
//https://stackoverflow.com/users/9062752/oyeraghib


import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.appcompat.widget.Toolbar
import com.xbcad.partyandpetals.Admin_Adapter.BalloonAdapter
import com.xbcad.partyandpetals.R
import com.xbcad.partyandpetals.Admin_data.Balloon
import com.xbcad.partyandpetals.StockActivity
import com.xbcad.partyandpetals.databinding.ActivityStockBubbleBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class AdminLatexBalloonsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStockBubbleBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var balloonAdapter: BalloonAdapter
    private val balloonList = mutableListOf<Balloon>()
    private val db: FirebaseFirestore = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStockBubbleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set the background color to white
        binding.root.setBackgroundColor(resources.getColor(android.R.color.white))

        // Initialize RecyclerView
        recyclerView = binding.recyclerView
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Initialize Adapter
        balloonAdapter = BalloonAdapter(balloonList, { balloon ->
            // Handle Edit action
        }, { balloon ->
            // Handle Delete action
            balloonList.remove(balloon)
            balloonAdapter.notifyDataSetChanged()
        })

        recyclerView.adapter = balloonAdapter

        // Fetch latex balloons from Firestore
        fetchBalloons()

        // Back button (Toolbar) setup
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            val intent = Intent(this, StockActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchBalloons() {
        Log.d("Firestore", "Fetching  balloons...") // Log before query execution

        db.collection("products")
            .whereEqualTo("category", "Latex")
            .get()
            .addOnSuccessListener { documents ->
                Log.d("Firestore", "Query successful, ${documents.size()} documents found") // Log number of documents

                balloonList.clear() // Clear existing data
                for (document in documents) {
                    val balloon = document.toObject(Balloon::class.java)
                    Log.d("Firestore", "Fetched balloon: ${balloon.name}, ${balloon.category}, ${balloon.price}") // Log each balloon details
                    balloonList.add(balloon) // Add each balloon to the list
                }

                Log.d("Firestore", "Updating adapter with ${balloonList.size} balloons") // Log before updating adapter
                balloonAdapter.notifyDataSetChanged() // Notify adapter of data change
                Log.d("Firestore", "Adapter updated") // Log after adapter update
            }
            .addOnFailureListener { exception ->
                Log.w("Firestore", "Error getting documents: ", exception) // Log error
            }
    }
}