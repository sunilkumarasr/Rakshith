package com.village.villagevegetables.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.village.villagevegetables.Config.Preferences
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.AddressModelResponse
import com.village.villagevegetables.R

class CheckInDialogAddressAdapter(
    val context: Context,
    private val items: List<AddressModelResponse>,
    private val onItemClick: (AddressModelResponse) -> Unit
) : RecyclerView.Adapter<CheckInDialogAddressAdapter.ViewHolder>() {

    private var selectedPosition = -1  // To track the currently selected position


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val radioButton: RadioButton = itemView.findViewById(R.id.radioButton)
        val txtCity: TextView = itemView.findViewById(R.id.txtCity)
        val txtAddress: TextView = itemView.findViewById(R.id.txtAddress)
        val txtName: TextView = itemView.findViewById(R.id.txtName)
        val txtMobile: TextView = itemView.findViewById(R.id.txtMobile)

        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                itemView.startAnimation(animations)

                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    // Save the selected position in preferences
                    Preferences.saveStringValue(context, Preferences.addressPosition, position.toString())
                    selectedPosition = position  // Update the selected position
                    notifyDataSetChanged()  // Notify adapter to refresh the view
                    radioButton.isChecked = true  // Check the RadioButton
                    onItemClick(items[position])  // Trigger the callback
                }
            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.checkin_dialog_address_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        holder.txtCity.text = " "+item.city+" "
        holder.txtName.text = item.name
        holder.txtAddress.text = item.area+" ,"+item.landmark
        holder.txtMobile.text = item.mobileNo+" , "+item.alternateMobileNumber

        // Set the RadioButton checked state based on selectedPosition
        holder.radioButton.isChecked = position == selectedPosition

    }

    override fun getItemCount(): Int {
        return items.size
    }


}