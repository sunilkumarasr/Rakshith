package com.village.villagevegetables.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.village.villagevegetables.Activitys.AllProductsListActivity
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Logins.LoginActivity
import com.village.villagevegetables.Models.CategoryListResponse
import com.village.villagevegetables.R

class HomeCategoriesAdapter(
    private val context: Context,
    private val items: List<CategoryListResponse>,
    private val onItemClick: (CategoryListResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<HomeCategoriesAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBanner: ImageView = itemView.findViewById(R.id.imgBanner)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)

        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                itemView.startAnimation(animations)
                val position = adapterPosition
                if (position==0){
                    val intent = Intent(context, AllProductsListActivity::class.java)
                    context.startActivity(intent)
                    if (context is Activity) {
                        context.overridePendingTransition(R.anim.from_right, R.anim.to_left)
                    }
                }else{
                    if (position != RecyclerView.NO_POSITION) {
                        onItemClick(items[position])
                    }
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.banners_layout_items_list, parent, false)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.banners_layout_relative_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.categoryName

        if (position == 0){
            Glide.with(holder.imgBanner)
                .load(item.categoryImage)
                .error(R.drawable.vegitable_ic)
                .into(holder.imgBanner)
        }else{
            Glide.with(holder.imgBanner)
                .load(item.categoryImage)
                .error(R.drawable.logo)
                .into(holder.imgBanner)
        }

    }

    override fun getItemCount(): Int {
        return items.size
    }
}