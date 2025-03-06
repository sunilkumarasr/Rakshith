package com.royalit.rakshith.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.rakshith.Activitys.ProductsDetailsActivity
import com.royalit.rakshith.Adapters.Cart.CartItems
import com.royalit.rakshith.Config.ViewController
import com.royalit.rakshith.Models.ProductListResponse
import com.royalit.rakshith.R

class AllProductsAdapter(
    val context: Context,
    private val items: List<ProductListResponse>,
    private val cartList: List<CartItems>,
    var click: ProductItemClick?,
    var quantityChangeListener: CartItemQuantityChangeListener?
) : RecyclerView.Adapter<AllProductsAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val relative: RelativeLayout = itemView.findViewById(R.id.relative)
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtActualPrice: TextView = itemView.findViewById(R.id.txtActualPrice)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)
        val linearCount: LinearLayout = itemView.findViewById(R.id.linearCount)
        val linearDecrement: LinearLayout = itemView.findViewById(R.id.linearDecrement)
        val linearIncrement: LinearLayout = itemView.findViewById(R.id.linearIncrement)
        val cartQty: TextView = itemView.findViewById(R.id.cartQty)
        val addToCart: LinearLayout = itemView.findViewById(R.id.addToCart)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.product_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.imgProducts)
            .load(item.productImage)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.imgProducts)
        holder.txtTitle.text = item.productTitle
        holder.txtQuantity.text = item.quantity
        holder.txtOfferPrice.text =  "(Price : ₹"+item.offerPrice+" )"
        holder.cartQty.text = "0"
        for (j in cartList.indices) {
            if (cartList[j].product_id.toInt() == item.productsId.toInt()) {

                holder.cartQty.text = cartList[j].cart_quantity.toString()

                if (cartList[j].cart_quantity.toInt() > 0){
                    holder.linearCount.visibility = View.VISIBLE
                    holder.addToCart.visibility = View.GONE
                }

                val finalAmount: Int = item.offerPrice.toInt() * holder.cartQty.text.toString().toInt()
//                holder.txtTotalPrice.visibility = View.VISIBLE
//                holder.txtTotalPrice.text = "Total Price : ₹ "+finalAmount

                //setCartId
                item.cartId= cartList[j].id.toString()
            }
        }

        val cartQ = intArrayOf(holder.cartQty.text.toString().toInt())

        holder.linearDecrement.setOnClickListener {
            val animations = ViewController.animation()
            holder.linearDecrement.startAnimation(animations)
            if (cartQ[0] > 1) {
                cartQ[0]--
                holder.cartQty.text = "" + cartQ[0]
                holder.cartQty.text = cartQ[0].toString()
                val carstQty = holder.cartQty.text.toString()
                if(cartQ[0]==1){
                    click!!.onAddToCartClicked(item, carstQty,1)
                }else{
                    click!!.onAddToCartClicked(item, carstQty,1)
                }
                // holder.binding.addToCartBtn.performClick()
            }
        }

        holder.linearIncrement.setOnClickListener {
            val animations = ViewController.animation()
            holder.linearIncrement.startAnimation(animations)

            val cartQty = holder.cartQty.text.toString()
            if (item.maxOrderQuantity.toInt()<=cartQty.toInt()){
                ViewController.showToast(context,"Max Quantity only for "+item.maxOrderQuantity)
                return@setOnClickListener
            }
            if (item.stock == cartQty) {
                ViewController.showToast(context,"Stock Limit only " + item.stock)
            }else{
                cartQ[0]++
                if (item.maxOrderQuantity.toInt()<=cartQty.toInt()){
                    Toast.makeText(context, "Can't add Max Quantity for this Product", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                holder.cartQty.text = cartQ[0].toString()
                val cartQty1 = holder.cartQty.text.toString()
                holder.cartQty.text = "" + cartQ.get(0)


                if (!ViewController.noInterNetConnectivity(context)) {
                    ViewController.showToast(context, "Please check your connection ")
                } else {
                    if (cartQty1 == "1")
                        click!!.onAddToCartClicked(item, cartQty1,0)
                    else{
                        click!!.onAddToCartClicked(item, cartQty1,1)
                    }
                }

                // holder.binding.addToCartBtn.performClick()

            }
        }

        holder.itemView.setOnClickListener {
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

    }

    override fun getItemCount(): Int {
        return items.size
    }

    interface ProductItemClick {
        fun onProductItemClick(itemsData: ProductListResponse?)
        fun onAddToCartClicked(itemsData: ProductListResponse?, cartQty: String?, isAdd:Int)
    }

    interface CartItemQuantityChangeListener {
        fun onQuantityChanged(cartItem: ProductListResponse, newQuantity: Int)
        fun onDeleteCartItem(cartItem: ProductListResponse)
    }


}