package com.royalit.rakshith.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.royalit.rakshith.Models.IntroSlide
import com.royalit.rakshith.R

class IntroSliderAdapter(private val introSlides: List<IntroSlide>) :
    RecyclerView.Adapter<IntroSliderAdapter.IntroSlideViewHolder>() {

    inner class IntroSlideViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtTitle = view.findViewById<TextView>(R.id.txtTitle)
        private val txtDec = view.findViewById<TextView>(R.id.txtDec)
        private val imgLogo = view.findViewById<ImageView>(R.id.imgLogo)

        fun bind(introSlide: IntroSlide) {
            txtTitle.text = introSlide.title
            txtDec.text = introSlide.description
            imgLogo.setImageResource(introSlide.icon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): IntroSlideViewHolder {
        return IntroSlideViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.slider_item_container, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return introSlides.size
    }

    override fun onBindViewHolder(holder: IntroSlideViewHolder, position: Int) {
        holder.bind(introSlides[position])
    }
}