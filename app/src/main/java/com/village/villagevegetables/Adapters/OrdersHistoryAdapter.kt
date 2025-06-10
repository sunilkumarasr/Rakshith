package com.village.villagevegetables.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.OrderHistoryResponse
import com.village.villagevegetables.R

class OrdersHistoryAdapter(
    private val context: Context,
    private val items: List<OrderHistoryResponse>,
    private val onItemClick: (OrderHistoryResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<OrdersHistoryAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val txtOrderNumber: TextView = itemView.findViewById(R.id.txtOrderNumber)
        val txtOrderAmount: TextView = itemView.findViewById(R.id.txtOrderAmount)
        val txtOrderStatus: TextView = itemView.findViewById(R.id.txtOrderStatus)
        val txtOrderDate: TextView = itemView.findViewById(R.id.txtOrderDate)
        val txtTotalItems: TextView = itemView.findViewById(R.id.txtTotalItems)
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
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.myorder_history_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        Glide.with(context).load(item.productDetails[0].image).error(R.drawable.logo)
            .placeholder(R.drawable.logo)
            .into(holder.image)
        holder.txtOrderNumber.text = item.orderNumber
        holder.txtOrderDate.text = item.createdDate


        //price
        var sum = item.grandTotal.toDouble()
        // delivery charge
        if (!item.deliveryCharge.equals("")) {
            sum += item.deliveryCharge.toDouble()
        }
         // promo code discount
        if (!item.promocode.equals("")) {
            sum -= item.promocode.toDouble()
        }
        holder.txtOrderAmount.text = "₹$sum"


        //order status
        when (item.deliveryStatus) {
            "1" -> {
                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.blue))
                holder.txtOrderStatus.text = "Order Placed"
            }
            "2" -> {
                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.pending))
                holder.txtOrderStatus.text = "Confirmed"
            }
            "3" -> {
                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.statusBarBg))
                holder.txtOrderStatus.text = "Pickup"
            }
            "4" -> {
                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.green))
                holder.txtOrderStatus.text = "Delivered"
            }
            "5" -> {
                holder.txtOrderStatus.setTextColor(ContextCompat.getColor(holder.itemView.context, R.color.selectedRed))
                holder.txtOrderStatus.text = "Canceled"
            }
        }

        //total items size
        val itemsSize =item.productDetails as ArrayList
        holder.txtTotalItems.text = "("+itemsSize.size+(context.getString(R.string.Items) +")")

    }

    override fun getItemCount(): Int {
        return items.size
    }

}