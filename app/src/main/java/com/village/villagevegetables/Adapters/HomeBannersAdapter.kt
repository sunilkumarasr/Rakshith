package com.village.villagevegetables.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.village.villagevegetables.Activitys.ProductsDetailsActivity
import com.village.villagevegetables.Config.ViewController
import com.village.villagevegetables.Models.BannersResponse
import com.village.villagevegetables.R


class HomeBannersAdapter(
    private val context: Context,
    private val imageList: ArrayList<BannersResponse>,
    private val viewPager2: ViewPager2) :
    RecyclerView.Adapter<HomeBannersAdapter.ImageViewHolder>() {

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView);
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.home_banners_items_list, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {

        val item = imageList[position]

        Glide.with(holder.imageView)
            .load(item.fullPath)
            .error(R.drawable.logo)
            .into(holder.imageView)

        if (position == imageList.size-1){
            viewPager2.post(runnable)
        }

        holder.itemView.setOnClickListener {
            val animations = ViewController.animation()
            holder.itemView.startAnimation(animations)
            if (!item.url.equals("")){
                val intent = Intent(context, ProductsDetailsActivity::class.java).apply {
                    putExtra("productsId", item.url)
                }
                context.startActivity(intent)
                if (context is Activity) {
                    context.overridePendingTransition(R.anim.from_right, R.anim.to_left)
                }
            }
        }

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}