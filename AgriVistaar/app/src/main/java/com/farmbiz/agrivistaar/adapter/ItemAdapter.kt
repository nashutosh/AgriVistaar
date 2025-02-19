package com.farmbiz.agrivistaar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.farmbiz.agrivistaar.R

data class Item(
    val name: String,
    val seller: String,
    val price: String,
    val location: String,
    val rating: String,
    val imageResId: Int
)

class ItemAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {

    private var filteredItems: List<Item> = items

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
        val nameTextView: TextView = itemView.findViewById(R.id.nameTextView)
        val sellerTextView: TextView = itemView.findViewById(R.id.sellerTextView)
        val priceTextView: TextView = itemView.findViewById(R.id.priceTextView)
        val locationTextView: TextView = itemView.findViewById(R.id.locationTextView)
        val ratingTextView: TextView = itemView.findViewById(R.id.ratingTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rv_item, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = filteredItems[position]
        holder.imageView.setImageResource(item.imageResId)
        holder.nameTextView.text = item.name
        holder.sellerTextView.text = item.seller
        holder.priceTextView.text = item.price
        holder.locationTextView.text = item.location
        holder.ratingTextView.text = item.rating
    }

    override fun getItemCount(): Int = filteredItems.size

    fun filter(query: String) {
        filteredItems = if (query.isEmpty()) {
            items
        } else {
            items.filter {
                it.name.contains(query, ignoreCase = true) ||
                        it.seller.contains(query, ignoreCase = true) ||
                        it.location.contains(query, ignoreCase = true)
            }
        }
        notifyDataSetChanged()
        }
}