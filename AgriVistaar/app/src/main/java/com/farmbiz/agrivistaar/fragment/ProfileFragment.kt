package com.farmbiz.agrivistaar.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.farmbiz.agrivistaar.LogInActivity
import com.farmbiz.agrivistaar.MyAccount
import com.farmbiz.agrivistaar.OrdersActivity
import com.farmbiz.agrivistaar.PaymentMethodsActivity
import com.farmbiz.agrivistaar.R
import com.farmbiz.agrivistaar.SearchResultActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class ProfileFragment : Fragment() {

    private lateinit var logOut : LinearLayout
    private lateinit var myAccount: LinearLayout
    private lateinit var myOrders : LinearLayout
    private lateinit var paymentMethods: LinearLayout
    private lateinit var support: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        myAccount = view.findViewById<LinearLayout>(R.id.myAccount)
        myAccount.setOnClickListener {
            val intent = Intent(activity,MyAccount::class.java)
            activity?.startActivity(intent)
        }

        myOrders = view.findViewById(R.id.myOrders)
        myOrders.setOnClickListener {
            val intent = Intent(activity,OrdersActivity::class.java)
            activity?.startActivity(intent)
        }

        paymentMethods = view.findViewById(R.id.paymentMethods)
        paymentMethods.setOnClickListener {
            val intent = Intent(activity,PaymentMethodsActivity::class.java)
            activity?.startActivity(intent)
        }

        logOut = view.findViewById(R.id.logOut)
        logOut.setOnClickListener {
            Firebase.auth.signOut()
            val intent = Intent(activity,LogInActivity::class.java)
            activity?.startActivity(intent)
        }

        support = view.findViewById(R.id.support)
        support.setOnClickListener {
            val intent = Intent(activity, SearchResultActivity::class.java)
            startActivity(intent)
        }

        return view
    }

}