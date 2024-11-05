package com.royalit.rakshith.Adapters

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
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.CartModel
import com.royalit.rakshith.R

class CartAdapter(private val itemList: ArrayList<CartModel>, private val onClick: (CartModel, String) -> Unit) : RecyclerView.Adapter<CartAdapter.ItemViewHolder>() {

    // ViewHolder class to hold the views for each item
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relative: RelativeLayout = itemView.findViewById(R.id.relative)
        val delete: ImageView = itemView.findViewById(R.id.delete)
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtActualPrice: TextView = itemView.findViewById(R.id.txtActualPrice)
        val linearDecrement: LinearLayout = itemView.findViewById(R.id.linearDecrement)
        val linearIncrement: LinearLayout = itemView.findViewById(R.id.linearIncrement)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.imgProducts.setImageResource(item.imageResId) // Set image
        holder.txtTitle.text = item.title
        holder.txtOfferPrice.text = item.offerPrice

        val spannableString = SpannableString(item.actualPrice)
        spannableString.setSpan(StrikethroughSpan(), 0, spannableString.length, 0)
        holder.txtActualPrice.text = spannableString

        holder.delete.setOnClickListener {
            val animations = ViewController.animation()
            holder.delete.startAnimation(animations)
            onClick(item,"Delete")
        }
        holder.linearDecrement.setOnClickListener {
            val animations = ViewController.animation()
            holder.linearDecrement.startAnimation(animations)
            onClick(item,"Decrement")
        }
        holder.linearIncrement.setOnClickListener {
            val animations = ViewController.animation()
            holder.linearIncrement.startAnimation(animations)
            onClick(item,"Increment")
        }
        holder.relative.setOnClickListener {
            val animations = ViewController.animation()
            holder.relative.startAnimation(animations)
            onClick(item,"")
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}