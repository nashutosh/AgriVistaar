package com.farmbiz.agrivistaar.fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.farmbiz.agrivistaar.AddProductsActivity
import com.farmbiz.agrivistaar.R
import com.google.android.material.floatingactionbutton.FloatingActionButton

class DashBoardFragment : Fragment() {

    private lateinit var spinner: Spinner
    private lateinit var floatingActionButton: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_dashboard, container, false)

        // Initialize spinner
        spinner = view.findViewById(R.id.spinner)
        val spinnerList: List<String> = listOf("Last Month", "Last Week", "Last Day", "Last Hour")
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, spinnerList)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = arrayAdapter

        // Initialize floatingActionButton
        floatingActionButton = view.findViewById(R.id.btn_floating)

        // Set OnClickListener for the FloatingActionButton
        floatingActionButton.setOnClickListener {
            val intent = Intent(activity, AddProductsActivity::class.java)
            activity?.startActivity(intent) // Safe null check for activity
        }

        return view
        }
}
