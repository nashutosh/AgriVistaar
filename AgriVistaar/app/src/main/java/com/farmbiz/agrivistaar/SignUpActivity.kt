package com.farmbiz.agrivistaar

import android.content.Intent
import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.farmbiz.agrivistaar.databinding.ActivitySignUpBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private val binding: ActivitySignUpBinding by lazy {
        ActivitySignUpBinding.inflate(layoutInflater)
    }

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // Initialize Firebase Auth and Firestore
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            // User is already signed in
            binding.progressBar.visibility = android.view.View.VISIBLE
            binding.main.visibility = android.view.View.GONE

            checkUserTypeAndNavigate(currentUser)
        }

        // Handle window insets for edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Log in button click listener
        binding.btnLogIn.setOnClickListener {
            startActivity(Intent(this, LogInActivity::class.java))
            finish()
        }

        // Sign up button click listener
        binding.btnSignUp.setOnClickListener {
            val emailId: String = binding.etEmailId.text.toString().trim()
            val userName: String = binding.etName.text.toString().trim()
            val password: String = binding.etPassword.text.toString().trim()

            // Check if fields are empty
            if (emailId.isEmpty() || userName.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all the details", Toast.LENGTH_SHORT).show()
            } else if (password.length < 8) {
                Toast.makeText(this, "Password is too short! Need Min. 8 characters", Toast.LENGTH_SHORT).show()
            } else {
                // Register user with Firebase
                auth.createUserWithEmailAndPassword(emailId, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val selectedId = binding.radioGroup.checkedRadioButtonId

                            if (selectedId != -1) {
                                val selectedRadioButton = findViewById<RadioButton>(selectedId)
                                val userType = selectedRadioButton.text.toString()

                                // Save user type in Firestore
                                val userId = auth.currentUser?.uid
                                if (userId != null) {
                                    val userMap = hashMapOf(
                                        "userName" to userName,
                                        "email" to emailId,
                                        "userType" to userType
                                    )
                                    firestore.collection("userId").document(userId)
                                        .set(userMap)
                                        .addOnSuccessListener {
                                            // Navigate to LanguageSelectionActivity
                                            val intent = Intent(this, LanguageSelectionActivity::class.java)
                                            intent.putExtra("userType", userType)
                                            startActivity(intent)
                                            finish()
                                        }
                                        .addOnFailureListener { e ->
                                            Toast.makeText(this, "Error saving user data: ${e.message}", Toast.LENGTH_SHORT).show()
                                        }
                                }
                            } else {
                                Toast.makeText(this, "Please select Farmer or Buyer", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            val exception = task.exception
                            Toast.makeText(baseContext, "Authentication failed: ${exception?.message}", Toast.LENGTH_SHORT).show()
                            }
                        }

            }

        }
    }

    private fun checkUserTypeAndNavigate(user: FirebaseUser) {
        val userId = user.uid
        firestore.collection("userId").document(userId).get()
            .addOnSuccessListener { documentSnapshot ->
                val userType = documentSnapshot.getString("userType")
                if (userType != null) {
                    val intent = if (userType == "Farmer") {
                        Intent(this, FarmerHomePageActivity::class.java)
                    } else {
                        Intent(this, BuyerHomePageActivity::class.java)
                    }
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Error retrieving user type", Toast.LENGTH_SHORT).show()
                    binding.progressBar.visibility = android.view.View.GONE
                    binding.main.visibility = android.view.View.VISIBLE
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error retrieving user data", Toast.LENGTH_SHORT).show()
                binding.progressBar.visibility = android.view.View.GONE
                binding.main.visibility = android.view.View.VISIBLE
                }
        }
}