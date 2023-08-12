package com.dbvertex.job.peresentation.poses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPosesListBinding
import com.dbvertex.job.network.response.poses.PosesImagesDTO
import com.dbvertex.job.network.response.poses.SubcategoryDTO
import com.dbvertex.job.peresentation.auth.NetworkState
import com.bumptech.glide.Glide
import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter


class PosesList : Fragment(), onPosesSubcategoryClick, onPosesImagesClick {
    private var screenWidth = 0

    private lateinit var mBinding: FragmentPosesListBinding
    var clickevent = true
    var nextpage = false
    private val mPOsesViewmodel by activityViewModels<PosesViewmodel>()
    private lateinit var imagesAdapter: PosesImagesAdapter
    var recyclermanager: Boolean = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    companion object {
        val rv_subcategory_position = MutableLiveData<String>()
        var selectedItem = 0
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentPosesListBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PosesList
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mBinding.posesListBack.setOnClickListener {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.posesHome, true).build()
            findNavController().navigate(R.id.posesHome, null, navOptions)
        }
        val bundle = arguments
        if (bundle != null) {
            val categoryid = bundle.getString("categoryid")
            val categoryname = bundle.getString("categoryname")
            mBinding.poseslistTitle.text = "${categoryname.toString()}"

            mPOsesViewmodel.getSubcategory(categoryid.toString())
            mPOsesViewmodel.subcategorylist.observe(viewLifecycleOwner, Observer {

                if (it.isNullOrEmpty()) {
                    mBinding.noPosesImages.visibility = View.VISIBLE
                    val adapter = PosesSubcategoryAdapter(
                        JobApp.instance.applicationContext,
                        it as ArrayList<SubcategoryDTO>,
                        this
                    )
                    mBinding.posesListSubcategoryRv.adapter = adapter

                    mBinding.posesimageslistRv.visibility = View.GONE
                } else {
                    val adapter = PosesSubcategoryAdapter(
                        JobApp.instance.applicationContext,
                        it as ArrayList<SubcategoryDTO>,
                        this
                    )
                    mBinding.posesListSubcategoryRv.adapter = adapter
                    mBinding.posesListSubcategoryRv.setHasFixedSize(true)
                    mBinding.noPosesImages.visibility = View.GONE
                    if (nextpage == false) {
                        rv_subcategory_position.value = it[0].id
                    }
                    mBinding.posesListSubcategoryRv.smoothScrollToPosition(selectedItem)

                }


            })
            VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(mBinding.posesimageslistRv))

            mBinding.posesimageslistRv.setHasFixedSize(true)
            mPOsesViewmodel.posesImagesList.observe(viewLifecycleOwner, Observer {
                if (it.isNullOrEmpty()) {
                    mBinding.noPosesImages.visibility = View.VISIBLE
                    imagesAdapter = PosesImagesAdapter(
                        JobApp.instance.applicationContext,
                        it as ArrayList<PosesImagesDTO>,
                        this,
                        screenWidth
                    )
                    mBinding.posesimageslistRv.adapter = imagesAdapter


                } else {

                    imagesAdapter = PosesImagesAdapter(
                        JobApp.instance.applicationContext,
                        it as ArrayList<PosesImagesDTO>,
                        this, screenWidth
                    )
                    mBinding.posesimageslistRv.adapter = imagesAdapter
                    mBinding.noPosesImages.visibility = View.GONE
                    mBinding.posesimageslistRv.visibility = View.VISIBLE
                }

            })

        }

        rv_subcategory_position.observe(viewLifecycleOwner) {
            if (!it.isNullOrEmpty()) {
                if (clickevent) {
                    mPOsesViewmodel.getPosesImages(it)
                    clickevent = false
                }
            } else {
                mPOsesViewmodel.getPosesImages("-1")
            }
        }
        Glide.with(view).load(R.raw.progress_gif)
            .into(mBinding.imagesLoading)
        mPOsesViewmodel.categoryloading.observe(viewLifecycleOwner) { state ->
            when (state) {
                NetworkState.LOADING_STARTED -> {
                    mBinding.imagesLoading.visibility = View.VISIBLE
                    mBinding.posesimageslistRv.visibility = View.GONE
                }
                NetworkState.LOADING_STOPPED -> {
                    mBinding.imagesLoading.visibility = View.GONE
                    mBinding.posesimageslistRv.visibility = View.VISIBLE

                }
            }

        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // Handle the back button event

                val navOptions = NavOptions.Builder().setPopUpTo(R.id.posesHome, true).build()
                findNavController().navigate(R.id.posesHome, null, navOptions)

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(
            this.viewLifecycleOwner,
            onBackPressedCallback
        )


    }

    override fun onSubcategoryclick(subcategoryDTO: SubcategoryDTO) {
        clickevent = true
        nextpage = false
        rv_subcategory_position.value = subcategoryDTO.id
        val layoutManager2 = GridLayoutManager(JobApp.instance.applicationContext, 2)
        mBinding.posesimageslistRv.layoutManager = layoutManager2

    }

    override fun onImageLike(imagesDTO: PosesImagesDTO) {
        mPOsesViewmodel.setLikeUnlike(imagesDTO.id)
    }


    override fun onImageClick(imagesDTO: PosesImagesDTO, manage: Int) {
        nextpage = true
        val bundle = Bundle()
        bundle.putInt("position", manage)
        findNavController().navigate(R.id.posesDetail, bundle)

    }

    override fun onPosesAdd(imagesDTO: PosesImagesDTO) {
        Log.d("image_id", imagesDTO.id)
        val bundle = Bundle()
        bundle.putString("image_id", imagesDTO.id)
        findNavController().navigate(R.id.addPosesImage, bundle)
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        clickevent = true


    }


}