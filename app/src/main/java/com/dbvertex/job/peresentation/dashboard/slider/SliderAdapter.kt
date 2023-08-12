package com.dbvertex.job.peresentation.dashboard.slider

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.data.SliderItem
import com.bumptech.glide.Glide

class SliderAdapter(
    var context: Context,
    var list: List<SliderItem>,
    val onBannerClick: onBannerClick
)  : PagerAdapter(){
    // Layout Inflater
    lateinit var inflater: LayoutInflater
    override fun getCount(): Int {
        return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view ==`object` as ConstraintLayout

    override fun instantiateItem(container: ViewGroup, position: Int): Any {

        val image : ImageView
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_slider_home, container, false)
        image = view.findViewById(R.id.item_slider_imgs)
        Glide.with(JobApp.instance.applicationContext)
            .load(list[position].ImageUrl)
            .placeholder(R.color.blue_gray)
            .into(image)


     //   Toast.makeText(context, "this is : ${list[position].Intenturl}", Toast.LENGTH_SHORT).show()
        image.setOnClickListener {
     //  Toast.makeText(context, "this is : ${list[position].Intenturl}", Toast.LENGTH_SHORT).show()
           onBannerClick.bannerclick(list[position])
        }
        container.addView(view)
        return view


    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)

    }
}

interface onBannerClick{
    fun bannerclick(sliderItem: SliderItem)
}