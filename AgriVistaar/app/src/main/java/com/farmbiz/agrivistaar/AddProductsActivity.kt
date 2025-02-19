package com.farmbiz.agrivistaar

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.farmbiz.agrivistaar.files.Product
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.io.ByteArrayOutputStream
import java.util.UUID

class AddProductsActivity : AppCompatActivity() {

    private lateinit var addProducts: FirebaseFirestore
    private lateinit var editTextProductName: EditText
    private lateinit var spinnerProductCategory: Spinner
    private lateinit var editTextProductPrice: EditText
    private lateinit var editTextProductQuantity: EditText
    private lateinit var editTextProductDescription: EditText
    private lateinit var buttonSelectImage: Button
    private lateinit var buttonSubmitProduct: Button
    private lateinit var imageViewProductImage: ImageView

    private val REQUEST_IMAGE_CAPTURE = 1
    private val REQUEST_PERMISSIONS = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_add_products)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase
        FirebaseApp.initializeApp(this)
        addProducts = FirebaseFirestore.getInstance()

        // Initialize UI components
        editTextProductName = findViewById(R.id.editTextProductName)
        spinnerProductCategory = findViewById(R.id.spinnerProductCategory)
        editTextProductPrice = findViewById(R.id.editTextProductPrice)
        editTextProductQuantity = findViewById(R.id.editTextProductQuantity)
        editTextProductDescription = findViewById(R.id.editTextProductDescription)
        buttonSelectImage = findViewById(R.id.buttonSelectImage)
        buttonSubmitProduct = findViewById(R.id.buttonSubmitProduct)
        imageViewProductImage = findViewById(R.id.imageViewProductImage)

        val categories: List<String> = listOf("Last Month", "Last Week", "Last Day", "Last Hour")
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, categories)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerProductCategory.adapter = arrayAdapter

        // Request necessary permissions
        requestPermissions()

        // Set button listeners
        buttonSelectImage.setOnClickListener {
            openCamera()
        }

        buttonSubmitProduct.setOnClickListener {
            if (isFormValid()) {
                storeDataInFirestore()
                startActivity(Intent(this, FarmerHomePageActivity::class.java))
            } else {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun isFormValid(): Boolean {
        val name = editTextProductName.text?.toString() ?: ""
        val category = spinnerProductCategory.selectedItem?.toString() ?: ""
        val priceText = editTextProductPrice.text?.toString() ?: ""
        val quantityText = editTextProductQuantity.text?.toString() ?: ""
        val description = editTextProductDescription.text?.toString() ?: ""

        return name.isNotEmpty() && category.isNotEmpty() && priceText.isNotEmpty() && quantityText.isNotEmpty() && description.isNotEmpty()
    }

    // Store product data in Firestore
    private fun storeDataInFirestore() {
        val name = editTextProductName.text?.toString() ?: " "
        val category = spinnerProductCategory.selectedItem?.toString() ?: " "
        val priceText = editTextProductPrice.text?.toString() ?: " "
        val quantityText = editTextProductQuantity.text?.toString() ?: " "
        val description = editTextProductDescription.text?.toString() ?: " "

        // Check if any field is empty and log details
        if (name.isEmpty() || category.isEmpty() || priceText.isEmpty() || quantityText.isEmpty() || description.isEmpty()) {
            Log.d("Firestore", "Please fill in all fields. Name: $name, Category: $category, Price: $priceText, Quantity: $quantityText, Description: $description")
            return
        }

        // Convert price and quantity, handling potential conversion errors
        val price = try {
            priceText.toDouble()
        } catch (e: NumberFormatException) {
            Log.d("Firestore", "Invalid price input: $priceText")
            return
        }

        val quantity = try {
            quantityText.toInt()
        } catch (e: NumberFormatException) {
            Log.d("Firestore", "Invalid quantity input: $quantityText")
            return
        }

        // Log product data to verify before storing it
        Log.d("Firestore", "Storing product with Name: $name, Category: $category, Price: $price, Quantity: $quantity, Description: $description")

        // Get the image bitmap from the ImageView
        val bitmap = (imageViewProductImage.drawable as BitmapDrawable).bitmap

        // Create a unique filename for the image
        val filename = UUID.randomUUID().toString() + ".jpg"

        // Create a reference to the Firebase Storage
        val storage = FirebaseStorage.getInstance("gs://img-products-kr")
        val storageRef = storage.reference

        // Create a reference to the image file in Firebase Storage
        val imageRef = storageRef.child("products/$filename")

        // Upload the image to Firebase Storage
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
        val data = baos.toByteArray()
        val uploadTask = imageRef.putBytes(data)

        uploadTask.addOnSuccessListener {
            // Get the download URL of the uploaded image
            imageRef.downloadUrl.addOnSuccessListener { uri ->
                // Create a product map to store in Firestore
                val product = Product(
                    name,
                    category,
                    price,
                    quantity,
                    description,
                    uri.toString() // Add the image URL to the product map
                )

                addProducts.collection("products").add(product).addOnSuccessListener {
                    Log.d("Firestore", "Product added successfully")
                }.addOnFailureListener { e ->
                    Log.d("Firestore", "Error adding product: ${e.message}")
                }
            }
        }.addOnFailureListener { e ->
            Log.d("Firestore", "Error uploading image: ${e.message}")
        }
    }

    // Open the camera to take a picture
    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (intent.resolveActivity(packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
        }
    }

    // Handle the result of the camera activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            val bitmap = data?.extras?.get("data") as Bitmap
            imageViewProductImage.setImageBitmap(bitmap)
        }
    }

    // Request camera and storage permissions
    private fun requestPermissions() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                REQUEST_PERMISSIONS
            )
        }
    }

    // Handle the result of permission requests
    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS) {
            if (grantResults.isNotEmpty() && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                Log.d("Permissions", "Camera and storage permissions are required")
            }
        }
    }
}