package com.village.villagevegetables.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.village.villagevegetables.Models.BannersResponse
import com.village.villagevegetables.R


class HomeBannersAdapter(private val imageList: ArrayList<BannersResponse>, private val viewPager2: ViewPager2) :
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

    }

    override fun getItemCount(): Int {
        return imageList.size
    }

    private val runnable = Runnable {
        imageList.addAll(imageList)
        notifyDataSetChanged()
    }
}