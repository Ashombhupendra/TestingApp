package com.dbvertex.job.peresentation.photoshoot.poses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemAddPosesToPhotoshootBinding
import com.dbvertex.job.network.response.photoshoot.ImagePhotoshootlistDTO

class AddImagePosesAdapter(private val list: List<ImagePhotoshootlistDTO>, private val onClickADD: onClickADD):
    RecyclerView.Adapter<AddImagePosesAdapter.AddViewHolder>()
{



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddViewHolder {
        val binding = ItemAddPosesToPhotoshootBinding.inflate(LayoutInflater.from(parent.context), parent, false)
          return AddViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AddViewHolder, position: Int) {
        holder.bind(list[position], onClickADD)
    }

    override fun getItemCount(): Int {
       return list.size
    }
    class AddViewHolder(private val mBinding : ItemAddPosesToPhotoshootBinding):RecyclerView.ViewHolder(mBinding.root)
    {
        fun bind(photoshootlistDTO: ImagePhotoshootlistDTO, onClickADD: onClickADD) {
                       mBinding.photoshootsdto = photoshootlistDTO

            mBinding.itemPhotoshootEdit.setOnClickListener {
                if (photoshootlistDTO.isAdded){
                     onClickADD.onDone(photoshootlistDTO)
                }else{

                    onClickADD.onAdd(photoshootlistDTO)

                }
            }
        }

    }
}
interface onClickADD{
      fun onAdd(photoshootlistDTO: ImagePhotoshootlistDTO)

      fun onDone(photoshootlistDTO: ImagePhotoshootlistDTO)
}