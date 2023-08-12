package com.dbvertex.job.peresentation.poses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPosesHomeBinding
import com.dbvertex.job.network.response.poses.CategoryDTO
import com.dbvertex.job.peresentation.auth.NetworkState
import com.bumptech.glide.Glide
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter


class PosesHome : Fragment() , OnPosesClick {

           private lateinit var mBinding : FragmentPosesHomeBinding
    private val mPOsesViewmodel by activityViewModels<PosesViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentPosesHomeBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PosesHome
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mPOsesViewmodel.getPosesCategory()
        mPOsesViewmodel.PosescategoryList.observe(viewLifecycleOwner){
            val adapter = PosesCategoryAdapter(it as ArrayList<CategoryDTO>, this)
            mBinding.posesCategoryRv.adapter = adapter

        }
        Glide.with(view).load(R.raw.progress_gif)
            .into(mBinding.categoryLoading)
        mPOsesViewmodel.categoryloading.observe(viewLifecycleOwner){ state ->
            when(state){
                NetworkState.LOADING_STARTED ->{
                    mBinding.categoryLoading.visibility = View.VISIBLE
                }
                NetworkState.LOADING_STOPPED ->{
                    mBinding.categoryLoading.visibility = View.GONE

                }
            }

        }

        VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(mBinding.posesCategoryRv))


        
    }

    override fun onClick(categoryDTO: CategoryDTO) {
        val bundle = Bundle()
        bundle.putString("categoryid", "${categoryDTO.id}")
        bundle.putString("categoryname", "${categoryDTO.name}")
        findNavController().navigate(R.id.posesList, bundle)
    }

}