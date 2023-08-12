package com.dbvertex.job.peresentation.introduction

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.dbvertex.job.R
import com.dbvertex.job.data.IntroItem

class IntroPagerAdapter(
   var  context :Context,
   var list : List<IntroItem>
) : PagerAdapter() {

    // Layout Inflater
    lateinit var inflater: LayoutInflater
    override fun getCount(): Int {
         return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view ==`object` as ConstraintLayout


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
           val image : ImageView
           val title : TextView
           val description : TextView

        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_introduction, container, false)

             image = view.findViewById(R.id.intro_image)
              title = view.findViewById(R.id.intro_title)
               description = view.findViewById(R.id.intro_description)


              Log.d("Imagelist ", list[position].toString())
           title.setText(list[position].title)
        if (position == 0){
            title.textAlignment = TextView.TEXT_ALIGNMENT_CENTER
        }
           description.text = list[position].description

        image.setOnClickListener {
       //     Toast.makeText(context, "This", Toast.LENGTH_SHORT).show()
        }
      //  Glide.with(context).load(list[position].Image).into(image)
       container.addView(view)
        return view

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)

    }
}