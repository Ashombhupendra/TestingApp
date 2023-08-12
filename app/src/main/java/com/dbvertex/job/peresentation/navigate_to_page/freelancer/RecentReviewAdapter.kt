package com.dbvertex.job.peresentation.navigate_to_page.freelancer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemRecentReviewsBinding
import com.dbvertex.job.network.response.ratingres.RecentReviewDTO
import com.squareup.picasso.Picasso

class RecentReviewAdapter(context : Context , val list : ArrayList<RecentReviewDTO>):
   RecyclerView.Adapter<RecentReviewAdapter.RecentviewHolder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentviewHolder {
        val binding = ItemRecentReviewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RecentviewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecentviewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class RecentviewHolder(val mBinding: ItemRecentReviewsBinding): RecyclerView.ViewHolder(mBinding.root)  {
        fun bind(reviewDTO: RecentReviewDTO) {
             mBinding.itemReviewName.text = "${reviewDTO.First_name} ${reviewDTO.last_name}"
            mBinding.itemReviewUsercategory.text = reviewDTO.user_category
            mBinding.itemReviewComment.text = reviewDTO.comment
            Picasso.get().load(reviewDTO.profile_pic).into(mBinding.itemReviewsProfilepic)
        }

    }

}