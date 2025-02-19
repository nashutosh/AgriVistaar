package com.farmbiz.agrivistaar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.farmbiz.agrivistaar.R
import com.farmbiz.agrivistaar.files.Product

class ProductAdapter : RecyclerView.Adapter<ProductAdapter.ViewHolder>() {
    private val products = mutableListOf<Product>()

    fun setProducts(products: List<Product>) {
        this.products.clear()
        this.products.addAll(products)
        notifyDataSetChanged() // Notify the adapter that the data has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = products[position]
        holder.bind(product)
    }

    override fun getItemCount(): Int = products.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)
        private val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        private val sellerTextView: TextView = itemView.findViewById(R.id.sellerTextView)
        private val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        private val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        private val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)

        fun bind(product: Product) {
            // Set image using Glide or Picasso

            Glide.with(itemView.context)
                .load(product.imageUrl)
                .into(imageView)

            nameTextView.text = product.name
            sellerTextView.text = product.seller
            priceTextView.text = product.price.toString()
            locationTextView.text = product.location
            ratingTextView.text = product.rating.toString()
        }
    }
}