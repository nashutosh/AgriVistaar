package com.farmbiz.agrivistaar

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.farmbiz.agrivistaar.adapter.ProductAdapter
import com.farmbiz.agrivistaar.files.Product
import com.google.firebase.firestore.FirebaseFirestore

class SearchResultActivity : AppCompatActivity() {

    private lateinit var addProducts: FirebaseFirestore
    private lateinit var productAdapter: ProductAdapter
    private lateinit var searchEditText: EditText
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_result)

        addProducts = FirebaseFirestore.getInstance()
        recyclerView = findViewById(R.id.recyclerView)

        recyclerView.layoutManager = LinearLayoutManager(this)
        productAdapter = ProductAdapter()
        recyclerView.adapter = productAdapter

        fetchProductsFromFirestore()

        searchEditText = findViewById(R.id.searchEditText)

        // Get the search query from the intent
        val query = intent.getStringExtra("QUERY")
        if (query != null) {
            searchEditText.setText(query)
        }

        searchEditText.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || event?.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = searchEditText.text.toString()
                true
            } else {
                false
            }
            }
        }

    @SuppressLint("NotifyDataSetChanged")
    private fun fetchProductsFromFirestore() {
        addProducts.collection("products").get().addOnSuccessListener { querySnapshot ->
            val products = querySnapshot.toObjects(Product::class.java)
            Log.d("Firestore", "Fetched products: $products")
            productAdapter.setProducts(products)
            productAdapter.notifyDataSetChanged()
        }.addOnFailureListener { e ->
            Log.d("Firestore", "Error fetching products: ${e.message}")
        }
    }
}
