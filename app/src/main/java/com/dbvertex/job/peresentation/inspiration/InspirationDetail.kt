package com.dbvertex.job.peresentation.inspiration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.dbvertex.job.databinding.FragmentInspirationDetailBinding


class InspirationDetail : Fragment() {

            private lateinit var mBinding: FragmentInspirationDetailBinding
    private val mInspirationViewmodel by activityViewModels<InspirationViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentInspirationDetailBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@InspirationDetail
            viewmodel = mInspirationViewmodel

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        mInspirationViewmodel.likeunlike.observe(viewLifecycleOwner, Observer {

            if (it){
                mBinding.itemInpirationLike.visibility = View.VISIBLE
                mBinding.itemInpirationUnlike.visibility = View.GONE
            }else{
                mBinding.itemInpirationLike.visibility = View.GONE
                mBinding.itemInpirationUnlike.visibility = View.VISIBLE
            }

        })
        mBinding.itemInpirationLike.setOnClickListener {

            mInspirationViewmodel.setlikeUnlike(mInspirationViewmodel.minspiration_id.value.toString())
            mInspirationViewmodel.likeunlike.value = false
        }
        mBinding.itemInpirationUnlike.setOnClickListener {
            mInspirationViewmodel.setlikeUnlike(mInspirationViewmodel.minspiration_id.value.toString())
            mInspirationViewmodel.likeunlike.value = true
        }

        

    }


}