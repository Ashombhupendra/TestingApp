package com.dbvertex.job.peresentation.navigate_to_page.blog

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.network.response.blogdetailImageDTO
import com.bumptech.glide.Glide

class BlogDetailSlider(
    var  context : Context,
    var list : List<blogdetailImageDTO>
) : PagerAdapter(){

    lateinit var inflater: LayoutInflater
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view ==`object` as ConstraintLayout


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_blog_details_slider, container, false)

        val imageview = view.findViewById<ImageView>(R.id.item_blogdetail_iv)

        Log.d("imagedetail", list[position].image.toString())

        Glide.with(JobApp.instance.applicationContext)
            .load(list[position].image)
            .placeholder(R.color.blue_gray)
            .into(imageview)


        container.addView(view)
        return view

    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)

    }


}