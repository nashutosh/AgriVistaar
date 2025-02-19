package com.farmbiz.agrivistaar

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.farmbiz.agrivistaar.databinding.ActivityLanguageSelectionBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import java.util.Locale

class LanguageSelectionActivity : AppCompatActivity() {

    private val binding:ActivityLanguageSelectionBinding by lazy {
        ActivityLanguageSelectionBinding.inflate(layoutInflater)
    }
    private lateinit var LL_English: LinearLayout
    private lateinit var LL_Hindi: LinearLayout
    private lateinit var LL_Gujarati: LinearLayout
    private lateinit var btn_continue: Button
    private var selectedLayout: LinearLayout? = null


   /* private lateinit var auth: FirebaseAuth*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }




        LL_English = findViewById(R.id.LL_English)
        LL_Hindi = findViewById(R.id.LL_Hindi)
        LL_Gujarati = findViewById(R.id.LL_Gujarati)
        btn_continue = findViewById(R.id.btn_continue)

        // Set click listeners
        LL_English.setOnClickListener { selectLayout(it as LinearLayout) }
        LL_Hindi.setOnClickListener { selectLayout(it as LinearLayout) }
        LL_Gujarati.setOnClickListener { selectLayout(it as LinearLayout) }

        btn_continue.setOnClickListener {
            val userType = intent.getStringExtra("userType")

            // Use the userType variable to navigate to the corresponding activity
            if (userType != null) {
                when (userType) {
                    "Farmer" -> {
                        val intent = Intent(this, FarmerHomePageActivity::class.java)
                        startActivity(intent)
                        finish() // Finish LanguageSelectionActivity after navigating
                    }
                    "Buyer" -> {
                        val intent = Intent(this, BuyerHomePageActivity::class.java)
                        startActivity(intent)
                        finish() // Finish LanguageSelectionActivity after navigating
                    }
                }
            }
        }
    }

    private fun selectLayout(layout: LinearLayout) {
        // Unselect the previously selected layout
        selectedLayout?.setBackgroundResource(R.drawable.bg_language_selection)

        // Select the new layout
        layout.setBackgroundResource(R.drawable.bg_selected)
        selectedLayout = layout

        when (layout.id) {
            R.id.LL_English -> setLanguage("en")
            R.id.LL_Hindi -> setLanguage("hi")
            R.id.LL_Gujarati -> setLanguage("gu")
        }

        // Enable the Continue button
        btn_continue.isEnabled = true
    }

    private fun setLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        baseContext.resources.updateConfiguration(config, baseContext.resources.displayMetrics)
    }
}
