package com.dbvertex.job.peresentation.favorites

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.databinding.ItemSliderHomeBinding
import com.dbvertex.job.network.response.inspiration.InspirationDTO
import com.dbvertex.job.network.response.inspiration.InspirationImagesDTO
import com.dbvertex.job.peresentation.custom_views.DoubleClickListener
import com.dbvertex.job.peresentation.inspiration.onInpirationalCLick
import com.bumptech.glide.Glide

class FavInspirationRVAdapter(
    var context: Context,
    var list: List<InspirationImagesDTO>,
    val onInpirationalCLick: onInpirationalCLick,
    val inspirationDTO: InspirationDTO,
    val mposition: Int
) : RecyclerView.Adapter<FavInspirationRVAdapter.FIViewHolder>() {
    class FIViewHolder(val binding : ItemSliderHomeBinding): RecyclerView.ViewHolder(binding.root) {
        fun bindView(
            list: List<InspirationImagesDTO>,
            onInpirationalCLick: onInpirationalCLick,
            inspirationDTO: InspirationDTO,
            mposition: Int
        ) {
          val   image = binding.itemSliderImgs


            image.setOnClickListener(object  : DoubleClickListener(){
                override fun onDoubleClick(v: View?) {
                    onInpirationalCLick.onInspirationDetail(inspirationDTO, mposition)
                }

            })
            Glide.with(JobApp.instance.applicationContext).load(list[position].images).centerCrop().into(image)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FIViewHolder {
        val binding = ItemSliderHomeBinding.inflate(LayoutInflater.from(parent.context),parent, false)
        return FIViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FIViewHolder, position: Int) {
        holder.bindView(list, onInpirationalCLick, inspirationDTO, mposition)

        Log.d("favinslist", list.toString())
    }

    override fun getItemCount(): Int {
       return  list.size
    }
}