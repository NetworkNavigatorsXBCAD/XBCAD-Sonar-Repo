package com.xbcad.partyandpetals.StockCategories

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.xbcad.partyandpetals.Admin_data.Balloon
import com.xbcad.partyandpetals.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class AddNewProductActivity : AppCompatActivity() {

    private lateinit var productNameEditText: EditText
    private lateinit var priceEditText: EditText
    private lateinit var categorySpinner: Spinner
    private lateinit var saveButton: Button
    private lateinit var selectImageButton: Button
    private lateinit var selectedImageView: ImageView

    private var imageUri: Uri? = null
    private lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_new_product)

        // Initialize views
        productNameEditText = findViewById(R.id.editProductName)
        priceEditText = findViewById(R.id.editPrice)
        categorySpinner = findViewById(R.id.spinnerCategory)
        saveButton = findViewById(R.id.saveButton)
        selectImageButton = findViewById(R.id.selectImageButton)
        selectedImageView = findViewById(R.id.selectedImageView)

        // Initialize Firebase Storage reference
        storageReference = FirebaseStorage.getInstance().reference

        // Back button functionality
        val backButton = findViewById<ImageButton>(R.id.backButton)
        backButton.setOnClickListener {
            finish() // Close the activity and go back
        }

        // Populate the category spinner
        val categories = listOf("Latex", "Foil", "Bubble")
        val adapter = ArrayAdapter(this, R.layout.spinner_item, categories)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter // Assign adapter to the spinner

        // Select Image button functionality
        selectImageButton.setOnClickListener {
            openImagePicker()
        }

        // Save button functionality
        saveButton.setOnClickListener {
            if (imageUri != null) {
                uploadImageAndSaveProduct()
            } else {
                Toast.makeText(this, "Please select an image", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun openImagePicker() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*" // Show only images
        startActivityForResult(intent, PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.data
            selectedImageView.setImageURI(imageUri)
        }
    }

    private fun uploadImageAndSaveProduct() {
        val productName = productNameEditText.text.toString()
        val price = priceEditText.text.toString()
        val category = categorySpinner.selectedItem.toString()

        if (imageUri != null) {
            val imageReference = storageReference.child("images/$productName.jpg") // Ensure the path has the right format and extension
            Log.d("ImagePath", "Uploading image to path: $imageReference")

            imageReference.putFile(imageUri!!)
                .addOnSuccessListener {
                    imageReference.downloadUrl.addOnSuccessListener { uri ->
                        // Use the URL here
                        val imageUrl = uri.toString()
                        Log.d("ImageUpload", "Image URL: $imageUrl")
                        saveProduct(productName, price, category, imageUrl)  // Pass imageUrl to Firestore
                    }
                }
                .addOnFailureListener { exception ->
                    Log.e("ImageUpload", "Error uploading image: ${exception.message}")
                }
        }
    }

    private fun saveProduct(name: String, price: String, category: String, imageUrl: String) {
        val parsedPrice = try {
            price.toDouble()  // Convert price to Double
        } catch (e: NumberFormatException) {
            // If the input is not a valid number, set the price to 0.0 or show an error
            Toast.makeText(this, "Invalid price. Please enter a valid number.", Toast.LENGTH_SHORT).show()
            return // Exit the function if price is invalid
        }

        // Convert parsed price (Double) to String before passing to the Balloon data class
        val balloon = Balloon(
            imageResId = imageUrl, // Use the image URL in imageResId
            imageUrl = imageUrl, // Also set the imageUrl field
            name = name,
            price = parsedPrice.toString(), // Convert to String
            category = category
        )

        // Get Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Save the product to Firestore under a "products" collection
        db.collection("products")
            .add(balloon) // Add the balloon to Firestore
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this, "Product added successfully", Toast.LENGTH_SHORT).show()
                Log.d("Firestore", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding product: ${e.message}", Toast.LENGTH_SHORT).show()
                Log.e("Firestore", "Error adding document", e)
            }
    }

    companion object {
        private const val PICK_IMAGE_REQUEST = 1
    }
}
