package com.dbvertex.job.peresentation.inspiration

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemInspirationalLayoutBinding
import com.dbvertex.job.network.response.inspiration.InspirationDTO


class InspirationAdapter(context: Context, val list: ArrayList<InspirationDTO>, val  onInpirationalCLick: onInpirationalCLick):
    RecyclerView.Adapter<InspirationAdapter.myViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewholder {
        val v = ItemInspirationalLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return myViewholder(v)
    }

    override fun onBindViewHolder(holder: myViewholder, position: Int) {
        holder.bind(list[position], onInpirationalCLick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class myViewholder(val mBinding : ItemInspirationalLayoutBinding) : RecyclerView.ViewHolder(mBinding.root) {
        fun bind(inspirationDTO: InspirationDTO, onInpirationalCLick: onInpirationalCLick) {

            mBinding.itemInpirationComment.setOnClickListener {
                 onInpirationalCLick.onInspirationCommentClick(inspirationDTO)
            }
            mBinding.itemInpirationFav.setOnClickListener {
                if (inspirationDTO.user_saved){
                    mBinding.itemInpirationFav.setImageResource(R.drawable.ic_bookmark)
                    inspirationDTO.user_saved = false
                }else{
                    mBinding.itemInpirationFav.setImageResource(R.drawable.ic_baseline_bookmark_24)
                    inspirationDTO.user_saved = true

                }
                onInpirationalCLick.onInspirationFavClick(inspirationDTO)
            }
            val adapter = InspirationPagerAdapter(JobApp.instance.applicationContext,
                inspirationDTO.images,
                onInpirationalCLick,
                inspirationDTO,
            position)
            mBinding.itemInpirationViewpager.adapter = adapter
            mBinding.itemInpirationViewpager.setOnClickListener {
                Toast.makeText(JobApp.instance.applicationContext,
                "Click",
                Toast.LENGTH_SHORT).show()
            }
            if (inspirationDTO.images.size > 1){
                mBinding.wormDotsIndicator.visibility = View.VISIBLE

            }else{
                mBinding.wormDotsIndicator.visibility = View.GONE
            }
             mBinding.wormDotsIndicator.setViewPager(mBinding.itemInpirationViewpager)
            if (inspirationDTO.user_liked){
                mBinding.itemInpirationLike.visibility = View.VISIBLE
                mBinding.itemInpirationUnlike.visibility = View.GONE

            }else{
                mBinding.itemInpirationLike.visibility = View.GONE
                mBinding.itemInpirationUnlike.visibility = View.VISIBLE
            }
            mBinding.itemInpirationLike.setOnClickListener {
                onInpirationalCLick.onInspirationlikeClick(inspirationDTO)
                inspirationDTO.user_liked = false
                mBinding.itemInpirationLike.visibility = View.GONE
                mBinding.itemInpirationUnlike.visibility = View.VISIBLE
            }
            mBinding.itemInpirationUnlike.setOnClickListener {
                onInpirationalCLick.onInspirationlikeClick(inspirationDTO)
                inspirationDTO.user_liked = true
                mBinding.itemInpirationLike.visibility = View.VISIBLE
                mBinding.itemInpirationUnlike.visibility = View.GONE
            }




            mBinding.itemInspirationCommentTv.text = "${inspirationDTO.body}"
            mBinding.itemInspirationLikeTv.text = "${inspirationDTO.total_likes}"
            mBinding.itemInspirationCreatetime.text = "${inspirationDTO.created}"

            if (inspirationDTO.user_saved){
                mBinding.itemInpirationFav.setImageResource(R.drawable.ic_baseline_bookmark_24)
            }else{
                mBinding.itemInpirationFav.setImageResource(R.drawable.ic_bookmark)
            }

        }

    }
}

interface onInpirationalCLick{

    fun onInspirationlikeClick(inspirationDTO: InspirationDTO)

    fun onInspirationCommentClick(inspirationDTO: InspirationDTO)

    fun onInspirationFavClick(inspirationDTO: InspirationDTO)

    fun onInspirationDetail(inspirationDTO: InspirationDTO, position: Int)

}