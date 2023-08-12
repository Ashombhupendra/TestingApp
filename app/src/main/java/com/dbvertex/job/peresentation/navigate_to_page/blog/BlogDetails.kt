package com.dbvertex.job.peresentation.navigate_to_page.blog

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.dbvertex.job.JobApp
import com.dbvertex.job.databinding.FragmentBlogDetailsBinding


class BlogDetails : Fragment() {

    private lateinit var mBinding: FragmentBlogDetailsBinding
    private val mBlogViewModel by activityViewModels<BlogViewModel>()
    private var currentItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = FragmentBlogDetailsBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@BlogDetails
            viewmodel = mBlogViewModel
        }
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mBinding.blogDetailBack.setOnClickListener {
            requireActivity().viewModelStore.clear()
            findNavController().navigateUp()
        }
        val bundle = arguments
        if (bundle != null){
            val userid = bundle.getString("blogid")
            mBlogViewModel.getSingleBlog(userid.toString())
            mBinding.fragmentBlogDetailFav.setOnClickListener {
                mBlogViewModel.setfav(blogid = userid.toString() )
                it.visibility = View.GONE
                mBinding.fragmentBlogDetailUnfav.visibility = View.VISIBLE
                val scaleXAnimator = ObjectAnimator.ofFloat(mBinding.fragmentBlogDetailUnfav, "scaleX", 1f, 2f, 1f)
                val scaleYAnimator = ObjectAnimator.ofFloat(mBinding.fragmentBlogDetailUnfav, "scaleY", 1f, 2f, 1f)
                val rotationAnimator = ObjectAnimator.ofFloat(mBinding.fragmentBlogDetailUnfav, "rotation", 0f, 45f, 0f)
                AnimatorSet().apply {
                    duration = 800
                    interpolator = OvershootInterpolator()
                    playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
                    start()
                }
            }
            mBinding.fragmentBlogDetailUnfav.setOnClickListener {
                mBlogViewModel.setfav(blogid = userid.toString() )
                it.visibility = View.GONE
                mBinding.fragmentBlogDetailFav.visibility = View.VISIBLE
                val scaleXAnimator = ObjectAnimator.ofFloat(mBinding.fragmentBlogDetailFav, "scaleX", 1f, 2f, 1f)
                val scaleYAnimator = ObjectAnimator.ofFloat(mBinding.fragmentBlogDetailFav, "scaleY", 1f, 2f, 1f)
                val rotationAnimator = ObjectAnimator.ofFloat(mBinding.fragmentBlogDetailFav, "rotation", 0f, 45f, 0f)
                AnimatorSet().apply {
                    duration = 800
                    interpolator = OvershootInterpolator()
                    playTogether(scaleXAnimator, scaleYAnimator, rotationAnimator)
                    start()
                }
            }
        }

        mBlogViewModel.singleblogItem.observe(viewLifecycleOwner, Observer {
          Log.d("imagedetail", it.toString())
            val blogAdapter = BlogDetailSlider(JobApp.instance.applicationContext, it)
            mBinding.blogdetailViewpager.adapter = blogAdapter
            if (it.size > 1){
                mBinding.previous.visibility =View.VISIBLE
                mBinding.next.visibility =View.VISIBLE
            }else{
                mBinding.previous.visibility =View.GONE
                mBinding.next.visibility =View.GONE
            }
            mBinding.wormDotsIndicator.setViewPager(mBinding.blogdetailViewpager)
        })

       mBlogViewModel.description.observe(viewLifecycleOwner){
           if (!it.isNullOrEmpty()){
               mBinding.blogdetailDescription.setText(Html.fromHtml(mBlogViewModel.description.value.toString()))
           }
       }
        mBlogViewModel.favboolean.observe(viewLifecycleOwner){
             if (it){
                 mBinding.fragmentBlogDetailUnfav.visibility = View.GONE
                 mBinding.fragmentBlogDetailFav.visibility = View.VISIBLE

             }else{
                 mBinding.fragmentBlogDetailUnfav.visibility = View.VISIBLE
                 mBinding.fragmentBlogDetailFav.visibility = View.GONE
             }
        }

        mBinding.blogdetailViewpager.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
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
                if (currentItem < mBinding.blogdetailViewpager.childCount+2){
                    mBinding.blogdetailViewpager.setCurrentItem(currentItem++ , true)
                }else{
                    currentItem= 0
                    mBinding.blogdetailViewpager.setCurrentItem(0 , true)

                }

                handler.postDelayed(this, 4000)
            }
        }, 4000)

        mBinding.next.setOnClickListener {
            if (currentItem < mBinding.blogdetailViewpager.childCount+2){
                mBinding.blogdetailViewpager.setCurrentItem(currentItem++ , true)
            }
        }

        mBinding.previous.setOnClickListener {
            if (currentItem != 0){

                mBinding.blogdetailViewpager.setCurrentItem(currentItem-1 , true)
            }
        }

    }
}