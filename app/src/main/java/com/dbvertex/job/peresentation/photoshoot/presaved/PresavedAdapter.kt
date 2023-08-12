package com.dbvertex.job.peresentation.photoshoot.presaved

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemPhotoshootPreSavedMsgBinding
import com.dbvertex.job.network.response.photoshoot.PresavedDTO

var category_name : String = ""
var boolean  : Boolean = true

class PresavedAdapter(val list: List<PresavedDTO>, val onPresavedClick: onPresavedClick) :
RecyclerView.Adapter<PresavedAdapter.presavedViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): presavedViewHolder {
        val binding = ItemPhotoshootPreSavedMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return presavedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: presavedViewHolder, position: Int) {

        holder.bind(list[position], onPresavedClick, position, list)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class presavedViewHolder(private val binding : ItemPhotoshootPreSavedMsgBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(dto: PresavedDTO, click: onPresavedClick, position: Int, list: List<PresavedDTO>) {


                      binding.itemPhotoshootPresavedCategory.text = "${dto.category_name}"
                      binding.itemPhotoshootPresavedMsg.text = "${dto.message}"

                binding.itemPhotoshootPresavedDetail.setOnClickListener {
                    click.onMessageClick(dto)
                }


            binding.itemPhotoshootPresavedAddNewMsg.setOnClickListener {
                 click.onAddMessageClick(list[position-1] )
            }
            binding.itemPhotoshootPresavedAddNewMsgLastItem.setOnClickListener {
                click.onAddMessageClick(dto)
            }

            if (!category_name.equals(dto.category_name) ){
                category_name = dto.category_name
                binding.itemPhotoshootPresavedAddNewMsgLastItem.visibility = View.GONE

                binding.itemPhotoshootPresavedCategory.visibility = View.VISIBLE
                if (position == 0){
                    binding.itemPhotoshootPresavedAddNewMsg.visibility =  View.GONE
                }else{
                    binding.itemPhotoshootPresavedAddNewMsg.visibility =  View.VISIBLE


                }
            }else{
                binding.itemPhotoshootPresavedAddNewMsg.visibility =  View.GONE
                binding.itemPhotoshootPresavedCategory.visibility =  View.GONE
                if (position == list.size -1){
                    binding.itemPhotoshootPresavedAddNewMsgLastItem.visibility = View.VISIBLE
                }else{
                    binding.itemPhotoshootPresavedAddNewMsgLastItem.visibility = View.GONE

                }

            }


        }

    }
}
interface onPresavedClick{
     fun onMessageClick(presavedDTO: PresavedDTO)

     fun  onAddMessageClick(presavedDTO: PresavedDTO)
}