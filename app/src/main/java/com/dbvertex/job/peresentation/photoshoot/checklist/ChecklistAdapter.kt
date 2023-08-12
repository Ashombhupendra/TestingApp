 package com.dbvertex.job.peresentation.photoshoot.checklist

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemPhotoshootPreSavedMsgBinding
import com.dbvertex.job.network.response.photoshoot.PhotoShootCheckListDTO

 var category_name : String = ""
var boolean  : Boolean = true
class ChecklistAdapter(val list: List<PhotoShootCheckListDTO>, val onChecklistClick: onChecklistClick) :
RecyclerView.Adapter<ChecklistAdapter.presavedViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): presavedViewHolder {
        val binding = ItemPhotoshootPreSavedMsgBinding.inflate(LayoutInflater.from(parent.context), parent, false)
         return presavedViewHolder(binding)
    }

    override fun onBindViewHolder(holder: presavedViewHolder, position: Int) {

        holder.bind(list[position], onChecklistClick, position, list)
    }

    override fun getItemCount(): Int {
        return list.size
    }


    class presavedViewHolder(private val binding : ItemPhotoshootPreSavedMsgBinding):RecyclerView.ViewHolder(binding.root) {
        fun bind(dto: PhotoShootCheckListDTO, click: onChecklistClick, position: Int, list: List<PhotoShootCheckListDTO>) {
                      binding.itemPhotoshootPresavedCategory.text = "${dto.category_name}"
                      binding.itemPhotoshootPresavedMsg.text = "${dto.title}"

                      binding.checkBox.visibility = View.VISIBLE
               binding.itemPhotoshootPresavedDetail.visibility = View.INVISIBLE

              binding.checkBox.isChecked = dto.status

            binding.checkBox.setOnClickListener {
                if (dto.status){
                    binding.checkBox.isChecked = false
                }else{
                    binding.checkBox.isChecked = true

                }
                 click.onStatusMessageClick(dto)
            }
            binding.itemPhotoshootPresavedMsg.setOnClickListener {
                click.onMessageClick(dto)
            }
            binding.itemPhotoshootPresavedAddNewMsg.setOnClickListener {
                Log.d("CategroyID", list[position-1].toString())
                click.onAddChecklistClick(list[position-1])
            }
            binding.itemPhotoshootPresavedAddNewMsgLastItem.setOnClickListener {
                Log.d("CategroyID", dto.toString())
                click.onAddChecklistClick(list[position])
            }

            if (!category_name.equals(dto.category_name) ){
                category_name = dto.category_name
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
interface onChecklistClick{
     fun onMessageClick(photoShootCheckListDTO: PhotoShootCheckListDTO)

     fun  onStatusMessageClick(photoShootCheckListDTO: PhotoShootCheckListDTO)

     fun  onAddChecklistClick(photoShootCheckListDTO: PhotoShootCheckListDTO)

}