package com.dbvertex.job.peresentation.favorites

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.databinding.ItemFavInspirationBinding

import com.dbvertex.job.network.response.inspiration.InspirationDTO
import com.dbvertex.job.peresentation.inspiration.onInpirationalCLick

class FavInspirationAdapter(context : Context,
                            val list : ArrayList<InspirationDTO>
,screenWidth: Int,
val onInpirationalCLick: onInpirationalCLick):
        RecyclerView.Adapter<FavInspirationAdapter.InspirationViewholder>(){

       private val context = context
    private val screenWidth = screenWidth

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InspirationViewholder {
        val binding = ItemFavInspirationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return InspirationViewholder(binding)
    }

    override fun onBindViewHolder(holder: InspirationViewholder, position: Int) {
        holder.bind(list[position],position , screenWidth,context, onInpirationalCLick)
    }

    override fun getItemCount(): Int {
        return list.size
    }
    class InspirationViewholder(val mBinding : ItemFavInspirationBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(
            dto: InspirationDTO,
            position: Int,
            screenWidth: Int,
            context: Context,
            onInpirationalCLick: onInpirationalCLick
        ) {
                  mBinding.itemFavInspirationTv.text = "${dto.body}"

          //  val adapter = InspirationGridAdapter(context = context , dto.images as ArrayList<InspirationImagesDTO>, screenWidth)
            /*val madapter = InspirationPagerAdapter(
                JobApp.instance.applicationContext,
                dto.images,
                onInpirationalCLick,
                dto,
                position
            )
            mBinding.itemInpirationViewpager.adapter = madapter*/

            val adapter = FavInspirationRVAdapter( JobApp.instance.applicationContext,
                dto.images,
                onInpirationalCLick,
                dto,
                position)

            mBinding.itemFavInspirationIv.adapter = adapter

            val pagerSnapHelper  = PagerSnapHelper()
            pagerSnapHelper.attachToRecyclerView(mBinding.itemFavInspirationIv)


            if (dto.images.size > 1){
                mBinding.wormDotsIndicator.visibility = View.VISIBLE
                Log.d("favchilecount", dto.images.size.toString())
            }else{
                mBinding.wormDotsIndicator.visibility = View.GONE
                Log.d("favchilecountq", dto.images.size.toString())
            }

            mBinding.wormDotsIndicator.setViewPager(mBinding.itemInpirationViewpager)
           // mBinding.itemFavInspirationIv.adapter = adapter

        }

    }
}