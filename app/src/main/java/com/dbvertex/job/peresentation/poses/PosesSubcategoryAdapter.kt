package com.dbvertex.job.peresentation.poses

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.network.response.poses.SubcategoryDTO


class PosesSubcategoryAdapter(
    context: Context,
    val list: ArrayList<SubcategoryDTO>,
    val onPosesSubcategoryClick: onPosesSubcategoryClick
):
    RecyclerView.Adapter<PosesSubcategoryAdapter.subcategoryViewholder>() {
    private val lastClickedPosition = -1
    companion object{

    }
    private val context: Context = context
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): subcategoryViewholder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_poses_list_subcategory,
            parent,
            false
        )
         return subcategoryViewholder(view)
    }

    override fun onBindViewHolder(holder: subcategoryViewholder, position: Int) {
        holder.bind(list[position], onPosesSubcategoryClick)
        holder.textview.setTextColor(context?.resources?.getColorStateList(R.color.grey))
        if (PosesList.selectedItem ==position){
            holder.textview.setTextColor(context?.resources?.getColorStateList(R.color.blue_dark))

        }

        holder.textview.setOnClickListener {
            val previousItem = PosesList.selectedItem
            PosesList.selectedItem = position
            notifyItemChanged(previousItem)
            notifyItemChanged(position)
            onPosesSubcategoryClick.onSubcategoryclick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return  list.size
    }

    class subcategoryViewholder(itemview: View): RecyclerView.ViewHolder(itemview) {

               val textview =  itemview.findViewById<TextView>(R.id.item_poses_subcategory)
        fun bind(dto: SubcategoryDTO, onPosesSubcategoryClick: onPosesSubcategoryClick) {

            textview.setText("${dto.name}")
          //  context?.resources?.getColorStateList(R.color.grey)


        }

    }
}

interface onPosesSubcategoryClick{
     fun onSubcategoryclick(subcategoryDTO: SubcategoryDTO)
}