package com.village.villagevegetables.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
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
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.ProductListResponse
import com.village.villagevegetables.R

class HomeFeatureProductsAdapter(
    val context: Context,
    private val items: MutableList<ProductListResponse>,
    private val cartList: List<CartItems>,
    var click: ProductItemClick?,
    var quantityChangeListener: CartItemQuantityChangeListener?
) : RecyclerView.Adapter<HomeFeatureProductsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtLabelOffer: TextView = itemView.findViewById(R.id.txtLabelOffer)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtActualPrice: TextView = itemView.findViewById(R.id.txtActualPrice)
        val txtItemType: TextView = itemView.findViewById(R.id.txtItemType)
        val linearCount: LinearLayout = itemView.findViewById(R.id.linearCount)
        val linearDecrement: LinearLayout = itemView.findViewById(R.id.linearDecrement)
        val linearIncrement: LinearLayout = itemView.findViewById(R.id.linearIncrement)
        val cartQty: TextView = itemView.findViewById(R.id.cartQty)
        val txtStock: TextView = itemView.findViewById(R.id.txtStock)
        val addToCart: LinearLayout = itemView.findViewById(R.id.addToCart)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.home_products_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        if (item.stock.toInt() ==0){
            holder.linearCount.visibility = View.GONE
            holder.addToCart.visibility = View.GONE
            holder.txtStock.visibility = View.VISIBLE
        }

        Glide.with(holder.imgProducts)
            .load(item.productImage)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.imgProducts)
        holder.txtTitle.text = item.productName
        holder.txtItemType.text = item.quantity
        holder.txtOfferPrice.text = "₹" + item.offerPrice
        holder.txtActualPrice.text = "₹" + item.salesPrice
        holder.txtActualPrice.paintFlags =
        holder.txtActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        holder.cartQty.text = "0"
        for (j in cartList.indices) {
            if (cartList[j].product_id.toInt() == item.productsId.toInt()) {

                //setCartId
                item.cartId = cartList[j].id.toString()

                holder.cartQty.text = cartList[j].cart_quantity.toString()

                if (cartList[j].cart_quantity.toInt() > 0) {
                    holder.linearCount.visibility = View.VISIBLE
                    holder.addToCart.visibility = View.GONE
                }

                val finalAmount: Int =
                    item.offerPrice.toInt() * holder.cartQty.text.toString().toInt()
//                holder.txtTotalPrice.visibility = View.VISIBLE
//                holder.txtTotalPrice.text = "Total Price : ₹ "+finalAmount

            }
        }


        holder.linearDecrement.setOnClickListener {
            val animations = ViewController.animation()
            holder.linearDecrement.startAnimation(animations)

            val cartQty = holder.cartQty.text.toString().toInt()
            val cartQ = intArrayOf(cartQty)

            if (cartQ[0] > 1) {
                cartQ[0]--
                holder.cartQty.text = "" + cartQ[0]
                holder.cartQty.text = cartQ[0].toString()
                val cartQty1 = holder.cartQty.text.toString()
                if (cartQ[0] == 1) {
                    click!!.onAddToCartClicked(item, cartQty1, 1)
                } else {
                    click!!.onAddToCartClicked(item, cartQty1, 1)
                }
                // holder.binding.addToCartBtn.performClick()
            } else if (cartQ[0] == 1) {
                //without api load
                holder.cartQty.text = "0"
                holder.addToCart.visibility = View.VISIBLE
                holder.linearCount.visibility = View.GONE
                //delete
                quantityChangeListener?.onDeleteCartItem(item)
            }

        }

        holder.linearIncrement.setOnClickListener {
            val animations = ViewController.animation()
            holder.linearIncrement.startAnimation(animations)

            val cartQty = holder.cartQty.text.toString().toInt()
            val cartQ = intArrayOf(cartQty)

            if (item.stock.toInt() <= cartQty) {
                ViewController.customToast(context,"Stock Limit only " + item.stock)
                return@setOnClickListener
            } else {
                cartQ[0]++
                holder.cartQty.text = cartQ[0].toString()
                val cartQty1 = holder.cartQty.text.toString()
                if (!ViewController.noInterNetConnectivity(context)) {
                    ViewController.customToast(context,"Please check your connection ")
                } else {
                    if (cartQ[0] == 1)
                        click!!.onAddToCartClicked(item, cartQty1, 0)
                    else {
                        click!!.onAddToCartClicked(item, cartQty1, 1)
                    }
                }
            }

        }

        holder.addToCart.setOnClickListener {
//            val animations = ViewController.animation()
//            holder.addToCart.startAnimation(animations)

            click!!.onAddToCartClicked(item, "1", 0)
            holder.addToCart.visibility = View.GONE
            holder.linearCount.visibility = View.VISIBLE
            holder.cartQty.text = "1"
        }

        holder.imgProducts.setOnClickListener {
            val animations = ViewController.animation()
            holder.imgProducts.startAnimation(animations)
            val intent = Intent(context, ProductsDetailsActivity::class.java).apply {
                putExtra("productsId", item.productsId)
            }
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
        }

        //label value
        val labelValue = item.salesPrice.toDouble() - item.offerPrice.toDouble()
        holder.txtLabelOffer.text = "₹" + labelValue.toInt()
    }

    override fun getItemCount(): Int {
        return 4
    }

    interface ProductItemClick {
        fun onProductItemClick(itemsData: ProductListResponse)
        fun onAddToCartClicked(itemsData: ProductListResponse, cartQty: String, isAdd: Int)
    }

    interface CartItemQuantityChangeListener {
        fun onQuantityChanged(cartItem: ProductListResponse, newQuantity: Int)
        fun onDeleteCartItem(cartItem: ProductListResponse)
    }


}