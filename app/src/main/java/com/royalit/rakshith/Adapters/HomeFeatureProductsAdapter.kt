package com.royalit.rakshith.Adapters

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.ProductListResponse
import com.royalit.rakshith.R

class HomeFeatureProductsAdapter(
    private val items: List<ProductListResponse>,
    private val onItemClick: (ProductListResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<HomeFeatureProductsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtActualPrice: TextView = itemView.findViewById(R.id.txtActualPrice)
        val txtItemType: TextView = itemView.findViewById(R.id.txtItemType)
        val txtLabelOffer: TextView = itemView.findViewById(R.id.txtLabelOffer)


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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_products_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imgProducts)
            .load(item.productImage)
            .error(R.drawable.logo)
            .into(holder.imgProducts)
        holder.txtTitle.text = item.productTitle
        holder.txtOfferPrice.text = "₹" + item.offerPrice
        holder.txtItemType.text = item.quantity

        holder.txtActualPrice.text = "₹" + item.salesPrice
        holder.txtActualPrice.paintFlags =
            holder.txtActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

        //label value
        val labelValue = item.salesPrice.toDouble() - item.offerPrice.toDouble()
        holder.txtLabelOffer.text = "₹" + labelValue.toInt()
    }

    override fun getItemCount(): Int {
        return items.size
    }

}