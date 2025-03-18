package com.village.villagevegetables.Adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.village.villagevegetables.Adapters.Cart.CartItems
import com.village.villagevegetables.Adapters.CartAdapter.ProductItemClick
import com.village.villagevegetables.Adapters.Search.SearchItems
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddressModel
import com.village.villagevegetables.Models.AddressModelResponse
import com.village.villagevegetables.R

class AddressAdapter(
    val context: Context,
    private val items: List<AddressModelResponse>,
    var click: ItemClick
) : RecyclerView.Adapter<AddressAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtCity: TextView = itemView.findViewById(R.id.txtCity)
        val txtAddress: TextView = itemView.findViewById(R.id.txtAddress)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtMobile: TextView = itemView.findViewById(R.id.txtMobile)
        val txtEdit: LinearLayout = itemView.findViewById(R.id.txtEdit)
        val txtDelete: LinearLayout = itemView.findViewById(R.id.txtDelete)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.txtCity.text = item.city
        holder.txtName.text = item.name
        holder.txtAddress.text = item.area+" ,"+item.landmark
        holder.txtMobile.text = item.mobileNo+" , "+item.alternateMobileNumber


        holder.txtEdit.setOnClickListener {
            click.onClicked(item, "edit")
        }
        holder.txtDelete.setOnClickListener {
            click.onClicked(item, "delete")

        }

    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ItemClick {
        fun onItemClick(itemsData: AddressModelResponse)
        fun onClicked(itemsData: AddressModelResponse, type: String)
    }


}