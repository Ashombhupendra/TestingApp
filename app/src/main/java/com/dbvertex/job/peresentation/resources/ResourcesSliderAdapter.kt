package com.dbvertex.job.peresentation.resources

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.dbvertex.job.R
import com.dbvertex.job.network.response.resources.ResourcesImageList
import com.bumptech.glide.Glide

class ResourcesSliderAdapter(var  context : Context,
                             var list: List<ResourcesImageList>
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

        Glide.with(context).load(list[position].image).placeholder(R.drawable.place_holder_img).into(image)
        container.addView(view)
        return view


    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)

    }
}