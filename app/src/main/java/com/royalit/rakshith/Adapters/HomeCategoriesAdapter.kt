package com.royalit.rakshith.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.rakshith.Models.HomeBannersModel
import com.royalit.rakshith.Models.HomeCategoriesModel
import com.royalit.rakshith.R

class HomeCategoriesAdapter(private val itemList: ArrayList<HomeCategoriesModel>) : RecyclerView.Adapter<HomeCategoriesAdapter.ItemViewHolder>() {

    // ViewHolder class to hold the views for each item
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBanner: ImageView = itemView.findViewById(R.id.imgBanner)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
       // val view = LayoutInflater.from(parent.context).inflate(R.layout.banners_layout_items_list, parent, false)
        val view = LayoutInflater.from(parent.context).inflate(R.layout.banners_layout_relative_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.imgBanner.setImageResource(item.imageResId) // Set image
        holder.txtTitle.text = item.title
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}