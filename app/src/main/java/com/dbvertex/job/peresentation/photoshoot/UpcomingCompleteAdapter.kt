package com.dbvertex.job.peresentation.photoshoot

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.network.response.photoshoot.Upcoming_complete_Photoshoot_DTO

class UpcomingCompleteAdapter(val list : List<Upcoming_complete_Photoshoot_DTO>, private val  onPhotoShootClick: onPhotoShootClick):
RecyclerView.Adapter<UpcomingCompleteAdapter.upcomingHolder>(){



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): upcomingHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_photoshoot_upcoming_complete, parent, false)

        return upcomingHolder(v)
    }

    override fun onBindViewHolder(holder: upcomingHolder, position: Int) {
        holder.bind(list[position], onPhotoShootClick)
    }

    override fun getItemCount(): Int {
       return  list.size
    }


    class upcomingHolder(itemview : View): RecyclerView.ViewHolder(itemview) {
        fun bind(
            photoshootDto: Upcoming_complete_Photoshoot_DTO,
            onPhotoShootClick: onPhotoShootClick
        ) {
             val titletext = itemView.findViewById<TextView>(R.id.item_photoshoot_title)
            val container = itemView.findViewById<ConstraintLayout>(R.id.item_photoshoot_container)

            val item_photoshoot_date_time = itemView.findViewById<TextView>(R.id.item_photoshoot_date_time)
              titletext.setText(photoshootDto.title)
            item_photoshoot_date_time.setText(photoshootDto.photoshoot_time)
            container.setOnClickListener {
                onPhotoShootClick.onItemClick(photoshootDto)
            }

        }

    }

}

interface onPhotoShootClick{
     fun onItemClick(upcomingCompletePhotoshootDto: Upcoming_complete_Photoshoot_DTO)
}