package com.royalit.rakshith.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.rakshith.Models.HomeProductsModel
import com.royalit.rakshith.R

class HomeProductsAdapter(private val itemList: ArrayList<HomeProductsModel> , private val onClick: (HomeProductsModel) -> Unit) : RecyclerView.Adapter<HomeProductsAdapter.ItemViewHolder>() {

    // ViewHolder class to hold the views for each item
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relative: RelativeLayout = itemView.findViewById(R.id.relative)
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        val addToCart: LinearLayout = itemView.findViewById(R.id.addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.products_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.imgProducts.setImageResource(item.imageResId) // Set image
        holder.txtTitle.text = item.title
        holder.txtPrice.text = item.price

        holder.relative.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return 4
    }
}