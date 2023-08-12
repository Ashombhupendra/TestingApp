package com.dbvertex.job.peresentation.poses

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.network.response.poses.PosesImagesDTO


class PosesDetail : Fragment(), onPosesImagesClick {

    private val mPOsesViewmodel by activityViewModels<PosesViewmodel>()
    private lateinit var imagesAdapter: PosesImagesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_poses_detail, container, false)
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

        val recyclerView = view.findViewById<ViewPager2>(R.id.posesdetail_rv)
        val poses_detail_back = view.findViewById<ImageView>(R.id.poses_detail_back)
        poses_detail_back.setOnClickListener {
            findNavController().navigateUp()
        }


        mPOsesViewmodel.posesImagesList.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                val adapter = PosesImagesAdapter(JobApp.instance.applicationContext, it as ArrayList<PosesImagesDTO>,
                    this, 1)
                recyclerView.adapter = adapter
                val bundle = arguments
                if (bundle != null){
                    val postion = bundle.getInt("position")
                    recyclerView.setCurrentItem(postion, false)
                }
            }
        }


    }

    override fun onImageLike(imagesDTO: PosesImagesDTO) {
        mPOsesViewmodel.setLikeUnlike(imagesDTO.id)
    }

    override fun onImageClick(imagesDTO: PosesImagesDTO, manage: Int) {

    }

    override fun onPosesAdd(imagesDTO: PosesImagesDTO) {
        val bundle = Bundle()
        bundle.putString("image_id", imagesDTO.id)
        findNavController().navigate(R.id.addPosesImage, bundle)
    }
}