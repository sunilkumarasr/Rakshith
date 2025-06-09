package com.village.villagevegetables.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.CategoryListResponse
import com.village.villagevegetables.R

class CategoriesNewAdapter(
    private val context: Context,
    private val items: List<CategoryListResponse>,
    private val categoriesId: String,
    private val onItemClick: (CategoryListResponse) -> Unit // Click listener function){}
) : RecyclerView.Adapter<CategoriesNewAdapter.ItemViewHolder>() {

    var previousSelectedIndex = -1
    var selectedIndex = items.indexOfFirst { it.categoriesId == categoriesId }.takeIf { it >= 0 } ?: 0

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgBanner: ImageView = itemView.findViewById(R.id.imgBanner)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val viewLine: View = itemView.findViewById(R.id.viewLine)

        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                itemView.startAnimation(animations)

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    previousSelectedIndex = selectedIndex
                    selectedIndex = position
                    notifyDataSetChanged() // Refresh all items to update backgrounds

                    onItemClick(items[position])
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.categories_new_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.categoryName

        Glide.with(holder.imgBanner)
            .load(item.categoryImage)
            .error(R.drawable.logo)
            .into(holder.imgBanner)

        // Apply background based on selectedIndex
        if (position == selectedIndex) {
            holder.viewLine.visibility = View.VISIBLE
            holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.black))

            val isMovingUp = selectedIndex < previousSelectedIndex
            holder.viewLine.visibility = View.VISIBLE

            holder.viewLine.translationY = if (isMovingUp) 40f else -40f
            holder.viewLine.alpha = 0f

            holder.viewLine.animate()
                .translationY(0f)
                .alpha(1f)
                .setInterpolator(AccelerateDecelerateInterpolator()) // Smooth in/out
                .setDuration(300)
                .start()


        } else {
            holder.viewLine.visibility = View.GONE
            holder.txtTitle.setTextColor(ContextCompat.getColor(context, R.color.txtH))

        }



    }

    override fun getItemCount(): Int {
        return items.size
    }
}