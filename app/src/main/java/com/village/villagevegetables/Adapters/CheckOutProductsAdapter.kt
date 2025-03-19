package com.village.villagevegetables.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.village.villagevegetables.Activitys.ProductsDetailsActivity
import com.village.villagevegetables.Adapters.Cart.CartItems
import com.village.villagevegetables.Api.RetrofitClient
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.R

class CheckOutProductsAdapter(
    val context: Context,
    private val items: List<CartItems>
) : RecyclerView.Adapter<CheckOutProductsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtActualPrice: TextView = itemView.findViewById(R.id.txtActualPrice)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtTotalPrice: TextView = itemView.findViewById(R.id.txtTotalPrice)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkout_products_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.imgProducts)
            .load(RetrofitClient.Image_URL2+item.product_image)
            .error(R.drawable.logo)
            .into(holder.imgProducts)
        holder.txtTitle.text = item.product_name
        holder.txtQuantity.text = item.quantity
        holder.txtOfferPrice.text =  "₹"+item.offer_price
        holder.txtActualPrice.text =  "₹"+item.sales_price
        holder.txtActualPrice.paintFlags =
            holder.txtActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        val finalAmount: Int = item.offer_price.toInt() * item.cart_quantity.toInt()
        holder.txtTotalPrice.text = "Total : ₹"+finalAmount

    }

    override fun getItemCount(): Int {
        return items.size
    }

}