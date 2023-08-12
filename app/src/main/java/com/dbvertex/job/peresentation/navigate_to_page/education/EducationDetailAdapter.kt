package com.dbvertex.job.peresentation.navigate_to_page.education

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.network.response.SingleSerieslist
import com.bumptech.glide.Glide

class EducationDetailAdapter(context: Context, val list: ArrayList<SingleSerieslist>,  val onContentClick: onSingleContentClick) :
  RecyclerView.Adapter<EducationDetailAdapter.viewholder>(){

    private val context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_education_detail, parent, false)

        return viewholder(v)
    }

    override fun onBindViewHolder(holder: viewholder, position: Int) {
        holder.bind(list[position], onContentClick)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class viewholder(itemView :View): RecyclerView.ViewHolder(itemView) {
        fun bind(serieslist: SingleSerieslist, onContentClick: onSingleContentClick) {
                val image = itemView.findViewById<ImageView>(R.id.item_education_secondary_iv)
              val title = itemView.findViewById<TextView>(R.id.item_education_secondary_tv_title)
            val like = itemView.findViewById<ImageView>(R.id.item_education_fav)
            val unlike = itemView.findViewById<ImageView>(R.id.item_education_unfav)
            val container = itemView.findViewById<ConstraintLayout>(R.id.item_secondary_container)

            if (serieslist.favourite){
                like.visibility = View.VISIBLE
                unlike.visibility = View.GONE
            }else{
                like.visibility = View.GONE
                unlike.visibility = View.VISIBLE
            }

            like.setOnClickListener {
                like.visibility = View.GONE
                unlike.visibility = View.VISIBLE
                val scaleXAnimator = ObjectAnimator.ofFloat(unlike, "scaleX", 1f, 1.8f, 1f)
                val scaleYAnimator = ObjectAnimator.ofFloat(unlike, "scaleY", 1f, 1.8f, 1f)
                val rotationAnimator = ObjectAnimator.ofFloat(unlike, "rotation", 0f, 45f, 0f)
                AnimatorSet().apply {
                    duration = 800
                    interpolator = OvershootInterpolator()
                    playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
                    start()
                }
                onContentClick.onContentLiked(itemView, serieslist)
            }
            unlike.setOnClickListener {
                like.visibility = View.VISIBLE
                unlike.visibility = View.GONE
                val scaleXAnimator = ObjectAnimator.ofFloat(like, "scaleX", 1f, 1.8f, 1f)
                val scaleYAnimator = ObjectAnimator.ofFloat(like, "scaleY", 1f, 1.8f, 1f)
                val rotationAnimator = ObjectAnimator.ofFloat(like, "rotation", 0f, 45f, 0f)
                AnimatorSet().apply {
                    duration = 800
                    interpolator = OvershootInterpolator()
                    playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
                    start()
                }
                onContentClick.onContentLiked(itemView, serieslist)
            }


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

interface onSingleContentClick{
    fun onnavContentDetail(itemview: View,  serieslist: SingleSerieslist)

    fun onContentLiked(itemview: View, serieslist: SingleSerieslist)
}
