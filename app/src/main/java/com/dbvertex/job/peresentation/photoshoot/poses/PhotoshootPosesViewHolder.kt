package com.dbvertex.job.peresentation.photoshoot.poses

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemPosesImagesBinding
import com.dbvertex.job.network.response.photoshoot.getPhotoshootPosesImagesDTO
import com.dbvertex.job.network.utils.setButtonAnimation
import com.bumptech.glide.Glide

class PhotoshootPosesViewHolder(private val binding: ItemPosesImagesBinding) :
    RecyclerView.ViewHolder(binding.root)  {

    fun  bind(photoshootPosesImagesDTO: getPhotoshootPosesImagesDTO,
      onPhotoshootPosesClick: onPhotoshootPosesClick){

        Log.d("poseslist", photoshootPosesImagesDTO.toString())

        Glide.with(itemView.context)
            .load(photoshootPosesImagesDTO.image)
            .placeholder(R.drawable.place_holder_img)
            .into(binding.itemPosesImagePic)
                 binding.itemPosesIamgeFav.visibility = View.GONE
        binding.itemPosesImagesUnfav.visibility = View.GONE
        binding.itemPosesImagesAdd.setImageResource(R.drawable.fav_poses_iv_dlt_icon)
        binding.itemPosesImagesAdd.setColorFilter(ContextCompat.getColor(itemView.context, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
        binding.itemPosesImagesAdd.setPadding(8)

//deleting
        binding.itemPosesImagesAdd.setOnClickListener {
            setButtonAnimation(it)
            onPhotoshootPosesClick.onClick(photoshootPosesImagesDTO)
        }

    }



    companion object {
        fun create(parent: ViewGroup): PhotoshootPosesViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ItemPosesImagesBinding.inflate(layoutInflater, parent, false)
            return PhotoshootPosesViewHolder(binding)
        }
    }
}