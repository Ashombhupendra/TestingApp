package com.dbvertex.job.peresentation.photoshoot.timeline

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentTimeLineBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.response.photoshoot.PhotoshootTimelineDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import kotlinx.coroutines.launch


class TimeLine : Fragment() , onTimelineClick{
            private lateinit var mBinding : FragmentTimeLineBinding
            val timelinelist = MutableLiveData<List<PhotoshootTimelineDTO>>()
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentTimeLineBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@TimeLine
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.itemPhotoshootPresavedAddNewMsg.setOnClickListener {
            if (mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
                Toast.makeText(JobApp.instance.applicationContext, "Firstly Save Photoshoot", Toast.LENGTH_SHORT).show()

            }else{
                findNavController().navigate(R.id.photoShootAddTimeLine)
            }

        }
        if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
            getTimeLine()
        }
        timelinelist.observe(viewLifecycleOwner){
            val adapter = PhotoshootTimelineAdapter(it, this)
            mBinding.psTimelineRv.adapter =adapter
            adapter.notifyDataSetChanged()
        }
    }

    private fun getTimeLine(){
        lifecycleScope.launch {
            val result = PhotoShootRepository.getPhotoShootTimeline(mPhotoshootViewModel.photoshoot_id.value.toString())
            when(result){
                is ResultWrapper.Success ->{
                    val list  = mutableListOf<PhotoshootTimelineDTO>()
                    list.addAll(result.response.map { it })
                    timelinelist.value = list

                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    override fun onItemClick(photoshootTimelineDTO: PhotoshootTimelineDTO) {
        lifecycleScope.launch {
            val result =PhotoShootRepository.getTimelinestaus(photoshootTimelineDTO.id)
            when(result){
                is ResultWrapper.Success ->{

                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }
}