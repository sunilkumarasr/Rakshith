package com.village.villagevegetables.Adapters

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.CategoryListResponse
import com.village.villagevegetables.R

class HomeCategoriesAdapter(
    private val context: Context,
    private val items: List<CategoryListResponse>,
    private val onItemClick: (CategoryListResponse) -> Unit
) : RecyclerView.Adapter<HomeCategoriesAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBanner: ImageView = itemView.findViewById(R.id.imgBanner)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)

        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                itemView.startAnimation(animations)
                val position = adapterPosition
                 onItemClick(items[position])
            }
        }

    }

//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
//        val view = LayoutInflater.from(parent.context)
//            .inflate(R.layout.banners_layout_relative_items_list, parent, false)
//        return ItemViewHolder(view)
//    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return try {
            val view = LayoutInflater.from(parent.context ?: throw IllegalStateException("Context is null"))
                .inflate(R.layout.banners_layout_relative_items_list, parent, false)
            ItemViewHolder(view)
        } catch (e: Exception) {
            Log.e("HomeCategoriesAdapter", "Error in onCreateViewHolder", e)
            // Return empty view holder or handle error appropriately
            throw e
        }
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.categoryName

        Glide.with(holder.imgBanner)
            .load(item.categoryImage)
            .error(R.drawable.logo)
            .into(holder.imgBanner)

    }

    override fun getItemCount(): Int {
        return items.size
    }
}