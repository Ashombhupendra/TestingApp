package com.dbvertex.job.peresentation.favorites

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentFavEducationBinding
import com.dbvertex.job.network.response.Serieslist


class FavEducation : Fragment(), onContentClick {
    private var screenWidth = 0
           private lateinit var mBinding : FragmentFavEducationBinding
           private val mFavoritesViewmodel by activityViewModels<FavoritesViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = FragmentFavEducationBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FavEducation

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        mFavoritesViewmodel.getFavEducation()
        mFavoritesViewmodel.educationlist.observe(viewLifecycleOwner, Observer {

            if (it.isNullOrEmpty()) {
                mBinding.favEducationNoItemFound.visibility = View.VISIBLE
                val adapter = FavEducationAdapter(
                    JobApp.instance.applicationContext,
                    it as ArrayList<Serieslist>,
                    this
                )
                mBinding.favEducationRv.adapter = adapter
            } else {
                mBinding.favEducationNoItemFound.visibility = View.GONE
                val adapter = FavEducationAdapter(
                    JobApp.instance.applicationContext,
                    it as ArrayList<Serieslist>,
                    this
                )
                mBinding.favEducationRv.adapter = adapter
            }

        })

    }

    override fun onnavContentDetail(itemview: View, serieslist: Serieslist) {
        val bundle = Bundle()
        bundle.putString("seriesid", serieslist.series_id)
        bundle.putString("contentid", serieslist.content_id)
        findNavController().navigate(R.id.educationDetail, bundle)
    }

    override fun onContentLiked(itemview: View, serieslist: Serieslist) {
        Log.d("this", "this")
    }


}