package com.dbvertex.job.peresentation.favorites

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.network.response.Serieslist
import com.bumptech.glide.Glide

class FavEducationAdapter(
    context: Context,
    val list: ArrayList<Serieslist>,
    val onContentClick: onContentClick
)
    : RecyclerView.Adapter<FavEducationAdapter.myViewHolder>(){

    private val context: Context = context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_fav_education, parent, false)

        return myViewHolder(v)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
        holder.bind(list[position], onContentClick)
    }

    override fun getItemCount(): Int {
       return list.size
    }
    class myViewHolder(itemview : View): RecyclerView.ViewHolder(itemview) {
        fun bind(serieslist: Serieslist, onContentClick: onContentClick) {
            val image = itemView.findViewById<ImageView>(R.id.item_fav_education_iv)
            val title = itemView.findViewById<TextView>(R.id.item_fav_education_title)
            val container = itemView.findViewById<ConstraintLayout>(R.id.item_fav_education_container)





            container.setOnClickListener {
                onContentClick.onnavContentDetail(itemView, serieslist)
            }
            title.text = "${serieslist.title}"
            Glide.with(JobApp.instance.applicationContext)
                .load(serieslist.image)
                .placeholder(R.color.blue_gray)
                .into(image)


        }

    }

}
interface onContentClick{
    fun onnavContentDetail(itemview: View,  serieslist: Serieslist)

    fun onContentLiked(itemview: View, serieslist: Serieslist)
}

