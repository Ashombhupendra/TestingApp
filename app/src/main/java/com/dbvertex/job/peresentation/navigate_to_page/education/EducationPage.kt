package com.dbvertex.job.peresentation.navigate_to_page.education

import android.os.Build
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentEducationPageBinding
import com.dbvertex.job.network.repository.EducationRepository
import com.dbvertex.job.network.response.EducationHighlightDTO
import com.dbvertex.job.network.response.EducationSeriesContent
import com.dbvertex.job.network.response.Serieslist
import com.dbvertex.job.network.utils.ResultWrapper
import com.google.android.material.transition.MaterialFadeThrough
import kotlinx.coroutines.launch

class EducationPage : Fragment() , onContentClick, onEducationBannerClick{
             private lateinit var mBinding: FragmentEducationPageBinding
    private val mEducationViewModel by activityViewModels<EducationViewModel>()
    private var currentItem = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentEducationPageBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@EducationPage

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mEducationViewModel.getHighlightContent()
        mEducationViewModel.highlightlist.observe(viewLifecycleOwner, Observer {

            val adapter = HighlightAdapter(JobApp.instance.applicationContext, it, this)
            mBinding.educationHeaderSlider.adapter = adapter
            mBinding.wormDotsIndicator.setViewPager(mBinding.educationHeaderSlider)
        })

           mEducationViewModel.getSeriesContent()
        mEducationViewModel.contentlist.observe(viewLifecycleOwner, Observer {
            val adapter = EducationContentAdapter(JobApp.instance.applicationContext, it  as ArrayList<EducationSeriesContent>, this)
            mBinding.rvEducationoSeries.adapter = adapter

        })

        mBinding.educationHeaderSlider.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
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
            @RequiresApi(Build.VERSION_CODES.Q)
            override fun run() {
                if (currentItem < mBinding.educationHeaderSlider.childCount+2){
                    mBinding.educationHeaderSlider.setCurrentItem(currentItem++ , true)
                }else{
                    currentItem = 0
                    mBinding.educationHeaderSlider.setCurrentItem(0 , false)
                }
                handler.postDelayed(this, 5000)
            }
        }, 5000)

    }

    override fun onnavContentDetail(itemview: View, serieslist: Serieslist) {
      //  Toast.makeText(requireContext(), "this  ${serieslist.content_id}", Toast.LENGTH_SHORT).show()
         val bundle = Bundle()
        bundle.putString("seriesid", serieslist.series_id)
        bundle.putString("contentid",serieslist.content_id)
        findNavController().navigate(R.id.educationDetail, bundle)
    }

    override fun onContentLiked(itemview: View, serieslist: Serieslist) {
      //  Toast.makeText(requireContext(), "this  ${serieslist.favourite}", Toast.LENGTH_SHORT).show()
        lifecycleScope.launch {
            val result = EducationRepository.SetfavUnFav(serieslist.content_id)
            when(result){
                is ResultWrapper.Success ->{

                }
                is ResultWrapper.Failure ->{

                }
            }
        }


    }

    override fun onBannerClick(educationHighlightDTO: EducationHighlightDTO) {
        val bundle = Bundle()
        bundle.putString("seriesid", educationHighlightDTO.series_id)
        bundle.putString("contentid",educationHighlightDTO.content_id)
        findNavController().navigate(R.id.educationDetail, bundle)
    }

}