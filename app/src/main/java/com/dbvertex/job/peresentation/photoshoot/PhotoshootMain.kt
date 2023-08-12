package com.dbvertex.job.peresentation.photoshoot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhotoshootMainBinding
import com.dbvertex.job.network.response.photoshoot.Upcoming_complete_Photoshoot_DTO


class PhotoshootMain : Fragment(), onPhotoShootClick {

    private lateinit var mBinding : FragmentPhotoshootMainBinding

    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    companion object{
        var photoshootid : String = ""
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = FragmentPhotoshootMainBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PhotoshootMain

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        mBinding.backOfflinePodcast.setOnClickListener {
            findNavController().navigateUp()
        }
         mPhotoshootViewModel.getUpcomingPhotoshoot()
        mPhotoshootViewModel.getCompletePhotoshoot()
        mPhotoshootViewModel.complete_photoshoot.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                mBinding.completePhotoshootEdit.setText("(${it.size})")
            }
            val madapter = UpcomingCompleteAdapter(it , this)
            mBinding.completePhotoshootRv.adapter = madapter
            madapter.notifyDataSetChanged()
        }
        mBinding.completePhotoshootRv.setHasFixedSize(true)
        mPhotoshootViewModel.upcoming_photoshoot.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                mBinding.upcomingEdit.setText("(${it.size})")
            }
            val adapter = UpcomingCompleteAdapter(it , this)
            mBinding.upcomingPhotoshootRv.adapter = adapter
            adapter.notifyDataSetChanged()

        }
        mBinding.upcomingPhotoshootRv.setHasFixedSize(true)

        mBinding.createPhotoshoot.setOnClickListener {

            val bundle = Bundle()
            bundle.putString("type_photoshoot", "create")
            findNavController().navigate(R.id.create_Photoshoot, bundle)
        }
    }

    override fun onItemClick(upcomingCompletePhotoshootDto: Upcoming_complete_Photoshoot_DTO) {
                mPhotoshootViewModel.photoshoot_id.value = upcomingCompletePhotoshootDto.id.toString()
       mPhotoshootViewModel.photoshoot_id.observe(viewLifecycleOwner){
           if (!it.isNullOrEmpty()){
               val bundle = Bundle()
               bundle.putString("type_photoshoot", "update")
               findNavController().navigate(R.id.create_Photoshoot, bundle)

               mPhotoshootViewModel.photoshoot_id.removeObservers(viewLifecycleOwner)
           }
       }
    }


}