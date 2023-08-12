package com.dbvertex.job.peresentation.dashboard.slider

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.dbvertex.job.R
import com.dbvertex.job.data.SliderItem
import com.squareup.picasso.Picasso

class CustomSliderAdapter internal  constructor(
     sliderItem: MutableList<SliderItem>,
     msliderItem: MutableList<SliderItem>,
     viewPager2: ViewPager2
) : RecyclerView.Adapter<CustomSliderAdapter.SliderViewHolder>() {
    private val sliderItem: List<SliderItem>
    private val viewPager2: ViewPager2
    init {
        this.sliderItem = sliderItem
        this.viewPager2 = viewPager2
    }
    class SliderViewHolder(itemview: View) : RecyclerView.ViewHolder(itemview) {
        fun bind(item: SliderItem) {
            val image = itemView.findViewById<ImageView>(R.id.item_slider_imgs)
            Picasso.get().load(item.ImageUrl).placeholder(R.drawable.place_holder_img).into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_slider_home, parent, false)
        return SliderViewHolder(view)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.bind(sliderItem[position])
        if (position == sliderItem.size - 4) {
        //    viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int {
        return sliderItem.size
    }


    private val runnable = Runnable {
        if (sliderItem.size > 30) {
            sliderItem.addAll(msliderItem)
            notifyDataSetChanged()
        }
    }
}