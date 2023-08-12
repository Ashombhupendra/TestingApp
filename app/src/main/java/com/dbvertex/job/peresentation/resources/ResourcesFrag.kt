package com.dbvertex.job.peresentation.resources

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isVisible
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentResourcesBinding
import com.dbvertex.job.network.response.resources.ResourcesDTO


class ResourcesFrag : Fragment(), OnResourcesClick {
    private lateinit var mBinding : FragmentResourcesBinding
    private val mResourcesViewModel by activityViewModels<ResourcesViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = FragmentResourcesBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ResourcesFrag

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
        mResourcesViewModel.getresourcesList()
        mResourcesViewModel.resourceslist.observe(viewLifecycleOwner, Observer {
            mBinding.noResourcesFound.isVisible = it.isNullOrEmpty()
            val adapter = ResourcesListAdapter(JobApp.instance.applicationContext, it as ArrayList<ResourcesDTO>, this)
            mBinding.rvResources.adapter = adapter
        })


    }

    override fun onResourcesItemClick(resourcesDTO: ResourcesDTO) {
        mResourcesViewModel.getResourcesdetail(resourcesDTO.resource_id)
       findNavController().navigate(R.id.resourcesDetails)
    }
}