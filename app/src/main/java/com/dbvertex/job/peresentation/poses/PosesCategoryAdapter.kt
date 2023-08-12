package com.dbvertex.job.peresentation.poses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemPosesCategoryBinding
import com.dbvertex.job.network.response.poses.CategoryDTO
import com.bumptech.glide.Glide

class PosesCategoryAdapter(val list  : ArrayList<CategoryDTO>, val onPosesClick: OnPosesClick):
      RecyclerView.Adapter<PosesCategoryAdapter.CategoryViewholder>(){


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewholder {
       val binding = ItemPosesCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewholder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewholder, position: Int) {
       holder.bind(list[position], onPosesClick)
    }

    override fun getItemCount(): Int {
        return  list.size
    }
    class CategoryViewholder(private val mBinding : ItemPosesCategoryBinding): RecyclerView.ViewHolder(mBinding.root) {
        fun bind(categoryDTO: CategoryDTO, onPosesClick: OnPosesClick) {
                   mBinding.itemPosesCategoryTv.text = "${categoryDTO.name}"
              Glide.with(itemView.context)
                .load(categoryDTO.img)
                .placeholder(R.drawable.place_holder_img)
                .into(mBinding.itemPosesCategoryIv)

                mBinding.posesWedding.setOnClickListener {
                    onPosesClick.onClick(categoryDTO)
                }
        }

    }
}
interface OnPosesClick{
     fun onClick(categoryDTO: CategoryDTO)
}