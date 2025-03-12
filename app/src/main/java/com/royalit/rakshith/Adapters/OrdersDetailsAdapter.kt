package com.royalit.rakshith.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.OrderHistoryProductDetailsResponse
import com.royalit.rakshith.R

class OrdersDetailsAdapter(
    private val context: Context,
    private val items: List<OrderHistoryProductDetailsResponse>,
    private val onItemClick: (OrderHistoryProductDetailsResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<OrdersDetailsAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
        val txtPrice: TextView = itemView.findViewById(R.id.txtPrice)
        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                itemView.startAnimation(animations)
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        // val view = LayoutInflater.from(parent.context).inflate(R.layout.banners_layout_items_list, parent, false)
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.myorder_details_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        Glide.with(context).load(item.image).error(R.drawable.logo)
            .placeholder(R.drawable.logo)
            .into(holder.image)

        holder.txtTitle.text = item.productName
        holder.txtQuantity.text = "Quantity : " + item.qty
        holder.txtPrice.text = "₹" + item.price

    }

    override fun getItemCount(): Int {
        return items.size
    }
}