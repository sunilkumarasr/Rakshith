package com.royalit.rakshith.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.rakshith.Activitys.ProductsDetailsActivity
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.FavouriteResponse
import com.royalit.rakshith.R

class FavouriteAdapter(
    private val context: Context,
    private val itemList: MutableList<FavouriteResponse>,
    ) : RecyclerView.Adapter<FavouriteAdapter.ItemViewHolder>() {

    // ViewHolder class to hold the views for each item
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relative: RelativeLayout = itemView.findViewById(R.id.relative)
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtActualPrice: TextView = itemView.findViewById(R.id.txtActualPrice)
        val txtItemType: TextView = itemView.findViewById(R.id.txtItemType)
        val linearRemove: LinearLayout = itemView.findViewById(R.id.linearRemove)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_products_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        Glide.with(holder.imgProducts)
            .load(item.productImage)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.imgProducts)
        holder.txtTitle.text = item.productTitle
        holder.txtOfferPrice.text = "₹"+item.offerPrice
        holder.txtItemType.text = item.quantity
        val spannableString = SpannableString("₹"+item.salesPrice)
        spannableString.setSpan(StrikethroughSpan(), 0, spannableString.length, 0)
        holder.txtActualPrice.text = spannableString


        holder.linearRemove.setOnClickListener {
            val animations = ViewController.animation()
            holder.linearRemove.startAnimation(animations)

            // Remove item from the list
            removeItem(position)
        }

        holder.itemView.setOnClickListener {
            val animations = ViewController.animation()
            holder.itemView.startAnimation(animations)
            val intent = Intent(context, ProductsDetailsActivity::class.java).apply {
                putExtra("productsId", item.productsId)
            }
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    private fun removeItem(position: Int) {
        if (position >= 0 && position < itemList.size) {
            itemList.removeAt(position)
            notifyItemRemoved(position)
            notifyItemRangeChanged(position, itemList.size) // Update indices
        }
    }

}