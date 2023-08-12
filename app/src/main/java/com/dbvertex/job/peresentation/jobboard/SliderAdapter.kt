package com.dbvertex.job.peresentation.jobboard

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import com.dbvertex.job.data.SliderItem
import com.dbvertex.job.databinding.SliderItemBinding
import com.bumptech.glide.Glide
import com.smarteist.autoimageslider.SliderViewAdapter


class SliderAdapter(val imageUrl: List<SliderItem>,val onBannerClick: onBannerClick) :
    SliderViewAdapter<SliderAdapter.SliderViewHolder>() {

    override fun getCount(): Int {
        return imageUrl.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapter.SliderViewHolder {
            val binding=SliderItemBinding.inflate(LayoutInflater.from(parent!!.context))
            return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapter.SliderViewHolder?, position: Int) {
        if (viewHolder != null) {
            Glide.with(viewHolder.itemView).load(imageUrl.get(position).ImageUrl.toString()).fitCenter()
                .into(viewHolder.imageView)

            viewHolder.imageView.setOnClickListener {
                onBannerClick.OnclickBannerImage(imageUrl.get(position))
            }


        }
    }

    class SliderViewHolder(val binding:SliderItemBinding) : SliderViewAdapter.ViewHolder(binding.root) {

        var imageView: ImageView = binding.myimage



    }
}
