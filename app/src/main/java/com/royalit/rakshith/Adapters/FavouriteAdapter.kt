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
import com.royalit.rakshith.Models.FavouriteModel
import com.royalit.rakshith.Models.HomeProductsModel
import com.royalit.rakshith.R

class FavouriteAdapter(private val itemList: ArrayList<FavouriteModel>, private val onClick: (FavouriteModel) -> Unit) : RecyclerView.Adapter<FavouriteAdapter.ItemViewHolder>() {

    // ViewHolder class to hold the views for each item
    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relative: RelativeLayout = itemView.findViewById(R.id.relative)
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtActualPrice: TextView = itemView.findViewById(R.id.txtActualPrice)
        val addToCart: LinearLayout = itemView.findViewById(R.id.addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.products_horizontal_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]
        holder.imgProducts.setImageResource(item.imageResId) // Set image
        holder.txtTitle.text = item.title
        holder.txtOfferPrice.text = item.price
        val spannableString = SpannableString("₹1000" )
        spannableString.setSpan(StrikethroughSpan(), 0, spannableString.length, 0)
        holder.txtActualPrice.text = spannableString

        holder.relative.setOnClickListener {
            onClick(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
}