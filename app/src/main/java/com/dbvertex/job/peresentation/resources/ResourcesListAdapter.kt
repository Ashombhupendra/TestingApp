package com.dbvertex.job.peresentation.resources

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.ItemResourcesBinding
import com.dbvertex.job.network.response.resources.ResourcesDTO
import com.bumptech.glide.Glide

class ResourcesListAdapter(context: Context, val list : ArrayList<ResourcesDTO>,val onresourcesClick: OnResourcesClick):
    RecyclerView.Adapter<ResourcesListAdapter.Myviewholder>() {

    private val context: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Myviewholder {
        val v = ItemResourcesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return Myviewholder(v)
    }

    override fun onBindViewHolder(holder: Myviewholder, position: Int) {
        holder.bind(list[position], onresourcesClick)
    }

    override fun getItemCount(): Int {
       return list.size
    }


    class Myviewholder(private val itemview : ItemResourcesBinding): RecyclerView.ViewHolder(itemview.root) {
        fun bind(resourcesDTO: ResourcesDTO, onresourcesClick: OnResourcesClick) {

            if (resourcesDTO.backimage != "" && resourcesDTO.backimage != null){
                Glide.with(JobApp.instance.applicationContext)
                    .load(resourcesDTO.backimage)
                    .placeholder(R.color.blue_gray)
                    .into(itemview.itemResourcesIv)

            }


            itemview.itemResourcesTv.text = "${resourcesDTO.name}"

            itemview.itemResourcesIv.setOnClickListener {
                onresourcesClick.onResourcesItemClick(resourcesDTO)
            }
        }

    }
}
interface OnResourcesClick{

    fun onResourcesItemClick(resourcesDTO: ResourcesDTO)
}