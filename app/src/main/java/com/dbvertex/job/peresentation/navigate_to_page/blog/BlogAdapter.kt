package com.dbvertex.job.peresentation.navigate_to_page.blog

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.R
import com.dbvertex.job.network.response.BlogDTO
import com.bumptech.glide.Glide
const val LEFT = 1
const val  RIGHT = 0
class BlogAdapter(val list : List<BlogDTO>,  context : Context, val onBlogClick: onBlogClick, val onFavClick: onFavClick): RecyclerView.Adapter<BlogAdapter.myViewHolder>() {

    private val context: Context = context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myViewHolder {
        val v = if (viewType == RIGHT) {
            LayoutInflater.from(parent.context).inflate(R.layout.item_blog_layout_left, parent, false)
        }   else{
            LayoutInflater.from(parent.context).inflate(R.layout.item_blog_layout_right, parent, false)
        }
        return myViewHolder(v)
    }

    override fun onBindViewHolder(holder: myViewHolder, position: Int) {
       holder.bind(list[position], onBlogClick , onFavClick)
     Log.d("blogsme", list[position].toString())
    }

    override fun getItemCount(): Int {
          return list.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(position % 2 ==1 ) LEFT else RIGHT
    }


    class myViewHolder(itemview : View) : RecyclerView.ViewHolder(itemview) {

        fun bind(blogDTO: BlogDTO, onBlogClick: onBlogClick, onFavClick: onFavClick) {

            val title = itemView.findViewById<TextView>(R.id.item_blog_title)
            val des = itemView.findViewById<TextView>(R.id.item_blog_description)
            val image  = itemView.findViewById<ImageView>(R.id.item_blog_iv)
            val date = itemView.findViewById<TextView>(R.id.item_blog_date)
            val fav = itemView.findViewById<ImageView>(R.id.item_blog_fav)
            val unfav = itemView.findViewById<ImageView>(R.id.item_blog_unfav)
            val layout = itemView.findViewById<ConstraintLayout>(R.id.ll_item_blog)

            layout.setOnClickListener {
                onBlogClick.navigateToblogDetails(it,blogDTO)
            }
            fav.setOnClickListener {

                fav.apply {
                    visibility = View.GONE
                }
                unfav.apply {
                    visibility = View.VISIBLE
                }
                val scaleXAnimator = ObjectAnimator.ofFloat(unfav, "scaleX", 1f, 1.5f, 1f)
                val scaleYAnimator = ObjectAnimator.ofFloat(unfav, "scaleY", 1f, 1.5f, 1f)
                val rotationAnimator = ObjectAnimator.ofFloat(unfav, "rotation", 0f, 45f, 0f)
                AnimatorSet().apply {
                    duration = 800
                    interpolator = OvershootInterpolator()
                    playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
                    start()
                }
                onFavClick.clickonFav(itemView, blogDTO)
            }
            unfav.setOnClickListener {

                fav.apply {
                    visibility = View.VISIBLE
                }
                unfav.apply {
                    visibility = View.GONE
                }
                val scaleXAnimator = ObjectAnimator.ofFloat(fav, "scaleX", 1f, 1.5f, 1f)
                val scaleYAnimator = ObjectAnimator.ofFloat(fav, "scaleY", 1f, 1.5f, 1f)
                val rotationAnimator = ObjectAnimator.ofFloat(fav, "rotation", 0f, 45f, 0f)
                AnimatorSet().apply {
                    duration = 800
                    interpolator = OvershootInterpolator()
                    playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
                    start()
                }
                onFavClick.clickonFav(itemView, blogDTO)
            }

            title.setText("${blogDTO.title}")
            des.setText(Html.fromHtml("${blogDTO.description}").toString())

            if (blogDTO.image != null) {
                Glide.with(itemView.context).load(blogDTO.image).into(image)
            }
            date.setText("${blogDTO.created}")

            if (blogDTO.favorite){
                fav.apply {
                    visibility = View.VISIBLE
                }
                unfav.apply {
                    visibility = View.GONE
                }
            }else{
                fav.apply {
                    visibility = View.GONE
                }
                unfav.apply {
                    visibility = View.VISIBLE
                }
            }

        }

    }
}

interface onBlogClick{
   fun navigateToblogDetails(itemview: View, blogDTO: BlogDTO)
}

interface onFavClick{
    fun clickonFav(itemview: View, blogDTO: BlogDTO)
}