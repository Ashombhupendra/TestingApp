package com.dbvertex.job.peresentation.photoshoot.presaved

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhotoshootPresavedMsgBinding
import com.dbvertex.job.network.response.photoshoot.PresavedDTO
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import com.google.gson.Gson


class Photoshoot_Presaved_Msg : Fragment(), onPresavedClick {
        private lateinit var mBinding : FragmentPhotoshootPresavedMsgBinding
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentPhotoshootPresavedMsgBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@Photoshoot_Presaved_Msg
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mPhotoshootViewModel.getPresavedMessage()
        mPhotoshootViewModel.pre_saved_msg_list.observe(viewLifecycleOwner){
            val adapter = PresavedAdapter(it, this)
            mBinding.presaveRv.adapter = adapter
            adapter.notifyDataSetChanged()
        }
        mBinding.presaveRv.setHasFixedSize(true)
    }

    override fun onMessageClick(presavedDTO: PresavedDTO) {

        val productDtoString = Gson().toJson(presavedDTO)
        findNavController().navigate(R.id.photoshoot_Presaved_detail,
            bundleOf("presaved" to productDtoString))


    }

    override fun onAddMessageClick(presavedDTO: PresavedDTO) {

       if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
           mPhotoshootViewModel.presavedTitle.value = ""
           mPhotoshootViewModel.presavedDescription.value = ""
           mPhotoshootViewModel.presavedCategoryID.value = presavedDTO.category_id
           findNavController().navigate(R.id.photoshoot_Presaved_detail)
       }else{
           showSnackBar("Firstly create Photoshoot")
       }

    }


}