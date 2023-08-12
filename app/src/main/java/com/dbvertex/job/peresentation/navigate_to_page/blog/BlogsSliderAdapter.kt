package com.dbvertex.job.peresentation.navigate_to_page.blog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewpager.widget.PagerAdapter
import com.dbvertex.job.R
import com.dbvertex.job.network.response.BlogBannerDto
import com.bumptech.glide.Glide

class BlogsSliderAdapter(
    var context: Context,
    var list: List<BlogBannerDto>,
    val onBlogBannerClick: onBlogBannerClick
) : PagerAdapter(){

    // Layout Inflater
    lateinit var inflater: LayoutInflater
    override fun getCount(): Int {
       return list.size
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean = view ==`object` as ConstraintLayout


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.item_blog_banner, container, false)

        val imageview = view.findViewById<ImageView>(R.id.iv_item_blog_banner)
        val item_blog_unfav = view.findViewById<ImageView>(R.id.item_blog_unfav)
        val item_blog_fav = view.findViewById<ImageView>(R.id.item_blog_fav)

        imageview.setOnClickListener {
            onBlogBannerClick.onBannerItemClick(list[position])
        }
        if (list[position].favorite){
            item_blog_fav.visibility= View.VISIBLE
            item_blog_unfav.visibility =View.GONE
        }else{
            item_blog_fav.visibility= View.GONE
            item_blog_unfav.visibility =View.VISIBLE
        }

        item_blog_fav.setOnClickListener {
            item_blog_fav.visibility= View.GONE
            item_blog_unfav.visibility =View.VISIBLE
            list[position].favorite = true
            onBlogBannerClick.onLikeUnlikebanner(list[position])
        }
        item_blog_unfav.setOnClickListener {
            item_blog_fav.visibility= View.VISIBLE
            item_blog_unfav.visibility =View.GONE
            list[position].favorite = false
            onBlogBannerClick.onLikeUnlikebanner(list[position])
        }


        val tvq =view.findViewById<TextView>(R.id.tv_item_blog_banner)

        tvq.setText(list[position].title)
       Glide.with(context).load(list[position].image).into(imageview)

        container.addView(view)
        return view

    }
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as ConstraintLayout)

    }

}
interface onBlogBannerClick{
   fun  onLikeUnlikebanner(blogbannerdto : BlogBannerDto)

   fun onBannerItemClick(blogbannerdto: BlogBannerDto)
}