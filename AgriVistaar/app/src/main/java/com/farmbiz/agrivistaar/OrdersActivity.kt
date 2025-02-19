package com.farmbiz.agrivistaar

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class OrdersActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_orders)

        // Setting padding for system bars (status bar, navigation bar)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Find the back icon ImageView and set a click listener
        val backButton = findViewById<ImageView>(R.id.icBack)
        backButton.setOnClickListener {
            // Call the onBackPressed method when the back icon is clicked
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        // You can perform custom actions here if needed before going back
        super.onBackPressed() // This will close the activity or go back
        }
}
