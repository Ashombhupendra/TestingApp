package com.dbvertex.job.peresentation.resources

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.databinding.FragmentResourcesDetailsBinding
import com.dbvertex.job.network.response.resources.ResourcesImageList


class ResourcesDetails : Fragment() {

    private lateinit var mBinding : FragmentResourcesDetailsBinding
    private val mResourcesViewModel by activityViewModels<ResourcesViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentResourcesDetailsBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ResourcesDetails
            viewmodel = mResourcesViewModel

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */)
        {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
          mResourcesViewModel.imagelist.observe(viewLifecycleOwner, Observer {
              val adapter = ResourcesSliderAdapter(JobApp.instance.applicationContext, it as List<ResourcesImageList>)
              mBinding.headerSlider.adapter = adapter
              mBinding.wormDotsIndicator.setViewPager(mBinding.headerSlider)
          })

           mBinding.back.setOnClickListener {
               findNavController().navigateUp()
           }
    }

}