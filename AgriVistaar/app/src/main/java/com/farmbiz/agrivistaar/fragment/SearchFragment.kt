package com.farmbiz.agrivistaar.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.SearchView
import com.farmbiz.agrivistaar.R
import com.farmbiz.agrivistaar.SearchResultActivity

class SearchFragment : Fragment() {

    private lateinit var searchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_search, container, false)

        searchView = view.findViewById(R.id.search_view)

        // Set up the query listener to open a new activity when search is submitted
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // When the search is submitted, open the SearchResultActivity
                val intent = Intent(activity, SearchResultActivity::class.java)
                // Optionally, you can pass the search query to the new activity
                intent.putExtra("QUERY", query)
                startActivity(intent)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // You can handle text changes here if needed
                return false
            }
        })

        return view
    }
}
