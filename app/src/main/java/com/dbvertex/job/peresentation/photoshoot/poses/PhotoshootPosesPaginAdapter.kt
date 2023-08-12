package com.dbvertex.job.peresentation.photoshoot.poses

import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.dbvertex.job.network.response.photoshoot.getPhotoshootPosesImagesDTO

class PhotoshootPosesPaginAdapter(
   private val onPhotoshootPosesClick: onPhotoshootPosesClick
) :PagingDataAdapter<getPhotoshootPosesImagesDTO, PhotoshootPosesViewHolder>(itemCallback){
    companion object {
        val itemCallback = object : DiffUtil.ItemCallback<getPhotoshootPosesImagesDTO>() {
            override fun areItemsTheSame(oldDto: getPhotoshootPosesImagesDTO, newDto: getPhotoshootPosesImagesDTO): Boolean {
                return oldDto == newDto
            }

            override fun areContentsTheSame(oldDto: getPhotoshootPosesImagesDTO, newDto: getPhotoshootPosesImagesDTO): Boolean {
                return oldDto.id == newDto.id
            }
        }
    }

    override fun onBindViewHolder(holder: PhotoshootPosesViewHolder, position: Int) {
        val posesImage = getItem(position) ?: return

        holder.bind(posesImage, onPhotoshootPosesClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoshootPosesViewHolder {
        return PhotoshootPosesViewHolder.create(parent)
    }

}
//deleting

interface onPhotoshootPosesClick{
    fun  onClick(photoshootPosesImagesDTO: getPhotoshootPosesImagesDTO)
}