package com.dbvertex.job.peresentation.navigate_to_page.blog


import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentBlogsPageBinding
import com.dbvertex.job.network.response.BlogBannerDto
import com.dbvertex.job.network.response.BlogDTO

import com.google.android.material.transition.MaterialFadeThrough
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter


class BlogsPage : Fragment(), onBlogClick ,onFavClick,onBlogBannerClick {
    private lateinit var mBinding: FragmentBlogsPageBinding
    lateinit var listme : ArrayList<BlogDTO>
    private var currentItem = 0
    private val mBlogViewModel by activityViewModels<BlogViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = FragmentBlogsPageBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@BlogsPage
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mBlogViewModel.getBlogbanners()
        mBlogViewModel.blogbannerlist.observe(viewLifecycleOwner, Observer {
            val adapter = BlogsSliderAdapter(JobApp.instance.applicationContext, it ,this)
            mBinding.blogViewpager.adapter = adapter

        })

        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 300 //You can manage the blinking time with this parameter
        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        mBinding.blogDate.startAnimation(anim)

        mBinding.blogViewpager.setPageTransformer(false, object  : ViewPager.PageTransformer{
            override fun transformPage(page: View, position: Float) {
                page.setAlpha(1 - Math.abs(position));
                if (position < 0) {
                    page.setScrollX(((page.getWidth()) * position).toInt());
                } else if (position > 0) {
                    page.setScrollX(- ( (page.getWidth()) * -position).toInt());
                } else {
                    page.setScrollX(0);
                }
            }

        })

        mBinding.blogViewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {}
            override fun onPageSelected(position: Int) {
                currentItem = position
            }
            override fun onPageScrollStateChanged(state: Int) {}
        })
        val handler = Handler()
        handler.postDelayed(object : Runnable {
            override fun run() {
                if (currentItem < mBinding.blogViewpager.childCount+2){
                    mBinding.blogViewpager.setCurrentItem(currentItem++ , true)
                }else{
                    currentItem= 0
                    mBinding.blogViewpager.setCurrentItem(0 , true)

                }

                handler.postDelayed(this, 4000)
            }
        }, 4000)
         mBinding.blogToPodcast.setOnClickListener {
             val bundle = Bundle()
             bundle.putString("1", "hello")
             findNavController().navigate(R.id.podcast, bundle)
         }
        mBinding.blogSearch.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("type", "blog")
            findNavController().navigate(R.id.searchDiscuss, bundle)

        }
        mBlogViewModel.getAllBlogs()
        mBlogViewModel.allblogs.observe(viewLifecycleOwner, Observer {
            Log.d("blogsme", it.toString())
            val adapter = BlogAdapter( it , JobApp.instance.applicationContext, this ,this)
            mBinding.blogRv.adapter = adapter
            adapter.notifyDataSetChanged()
        })
        mBinding.blogRv.setHasFixedSize(true)
        VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(mBinding.blogRv))



    }

    override fun navigateToblogDetails(itemview: View, blogDTO: BlogDTO) {
        val bundle = Bundle()
        bundle.putString("blogid", blogDTO.id)
        findNavController().navigate(R.id.blogDetails, bundle)


    }

    override fun onResume() {
        super.onResume()
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = 300 //You can manage the blinking time with this parameter
        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = Animation.INFINITE
        mBinding.blogDate.startAnimation(anim)
    }

    override fun onPause() {
        super.onPause()

    }
    override fun clickonFav(itemview: View, blogDTO: BlogDTO) {
        mBlogViewModel.setfav(blogid = blogDTO.id)
        if(mBlogViewModel.favboolean.value == true){
            mBlogViewModel.favboolean.value = false
        }else{
            mBlogViewModel.favboolean.value = true
        }
    }

    override fun onLikeUnlikebanner(blogbannerdto: BlogBannerDto) {
        mBlogViewModel.setfav(blogid = blogbannerdto.blogid)
    }


    override fun onBannerItemClick(blogbannerdto: BlogBannerDto) {
        val bundle = Bundle()
        bundle.putString("blogid", blogbannerdto.blogid )
        findNavController().navigate(R.id.blogDetails, bundle)
    }


}