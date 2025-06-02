package com.village.villagevegetables.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.PromoCodeItems
import com.village.villagevegetables.R

class PromoCodeAdapter(
    val context: Context,
    private val items: List<PromoCodeItems>,
    private val onItemClick: (PromoCodeItems) -> Unit // Click listener function
    ) : RecyclerView.Adapter<PromoCodeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDec: TextView = itemView.findViewById(R.id.txtDec)

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
            .inflate(R.layout.promocode_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.txtTitle.text = item.type
        holder.txtDec.text = "Minimum order amount: ₹" + item.amount

    }

    override fun getItemCount(): Int {
        return items.size
    }

}