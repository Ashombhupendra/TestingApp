package com.dbvertex.job.peresentation.inspiration

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter

import com.bumptech.glide.Glide
import com.dbvertex.job.R
import com.dbvertex.job.network.response.inspiration.InspirationDTO
import com.dbvertex.job.network.response.inspiration.InspirationImagesDTO
import com.dbvertex.job.peresentation.custom_views.DoubleClickListener

class InspirationPagerAdapter(
    var context: Context,
    var list: List<InspirationImagesDTO>,
    val onInpirationalCLick: onInpirationalCLick,
    val inspirationDTO: InspirationDTO,
    val mposition: Int
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

          image.setOnClickListener(object  : DoubleClickListener(){
              override fun onDoubleClick(v: View?) {
                  onInpirationalCLick.onInspirationDetail(inspirationDTO, mposition)
              }

          })
        Glide.with(context).load(list[position].images).centerCrop().into(image)
        container.addView(view)
        return view


    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)

    }

}