package com.dbvertex.job.peresentation.jobboard

import android.app.ActionBar

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.dbvertex.job.JobApp
import com.dbvertex.job.MainActivity
import com.dbvertex.job.R
import com.dbvertex.job.data.SliderItem
import com.dbvertex.job.databinding.ExitLayBinding
import com.dbvertex.job.databinding.FragmentJobBoardMainBinding
import com.dbvertex.job.network.repository.FreeLancerRepository
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.peresentation.jobboard.findjob.FindJobFrag
import com.dbvertex.job.peresentation.jobboard.managejob.ManageFrag
import com.dbvertex.job.peresentation.jobboard.postjob.PostJobFrag
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.android.material.transition.MaterialFadeThrough
import com.smarteist.autoimageslider.SliderView
import kotlinx.coroutines.launch

class JobBoardMain : Fragment(),onBannerClick {

    private lateinit var mBinding: FragmentJobBoardMainBinding

    lateinit var sliderView: SliderView
    val sliderlsit = MutableLiveData<List<SliderItem>>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 500
        }
    }

    companion object {
        val fragpostion = MutableLiveData<Int>(0)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentJobBoardMainBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@JobBoardMain
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val callback: OnBackPressedCallback =
            object : OnBackPressedCallback(true /* enabled by default */) {
                override fun handleOnBackPressed() {
                    Exit()

                }
            }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

//      getBanners()
//        sliderlsit.observe(viewLifecycleOwner, Observer {
//            var sliderAdapter = SliderAdapter(it,this)
//            sliderView.setSliderAdapter(sliderAdapter)
//            sliderAdapter.notifyDataSetChanged()
//
//        })

//        sliderView = mBinding.slider
//        sliderView.autoCycleDirection = SliderView.LAYOUT_DIRECTION_LTR
//        sliderView.scrollTimeInSec = 3
//        sliderView.isAutoCycle = true
//        sliderView.startAutoCycle()

        fragpostion.observe(viewLifecycleOwner) {
            mBinding.valueselected = it
            when (it) {
                0 -> {
                    loadFragment(FindJobFrag())
                    MainActivity.setheadline.value="Find Job"
                }
                1 -> {
                    loadFragment(PostJobFrag())
                    MainActivity.setheadline.value= "Post Job"
                }
                else -> {
                    Log.d("myuserid", SharedPrefrenceHelper.user.userid.toString())
                    loadFragment(ManageFrag())
                    MainActivity.setheadline.value="Manage Job"
                }
            }
        }


        mBinding.findjob.setOnClickListener {
            fragpostion.value = 0
        }

        mBinding.postjob.setOnClickListener {
            fragpostion.value = 1
        }

        mBinding.managejob.setOnClickListener {
            fragpostion.value = 2
        }
    }


    private fun getBanners() {
        Log.d("responsebanner","called");
        lifecycleScope.launch {
            val result = FreeLancerRepository.getBanner()
            when (result) {
                is ResultWrapper.Success -> {
                    val list = mutableListOf<SliderItem>()
                    list.addAll(result.response.map { it })
                    Log.d("responsebanner",result.response.toString());
                    sliderlsit.value = list
                }
                is ResultWrapper.Failure -> {
                    Log.d("responsebanner",result.errorMessage.toString());
                }
            }
        }
    }

    private fun Exit() {
        var alert: Dialog?
        val bnding = ExitLayBinding.inflate(LayoutInflater.from(requireContext()))
        alert = Dialog(requireContext(),R.style.Theme_AppCompat_Light_Dialog_Alert)
        alert.setContentView(bnding.root)
        bnding.ok.setOnClickListener {
            requireActivity().finish()
        }
        alert.getWindow()!!
            .setLayout(ActionBar.LayoutParams.FILL_PARENT, ActionBar.LayoutParams.WRAP_CONTENT);
        bnding.cancel.setOnClickListener {
            alert.cancel()
        }
        alert.show();
    }

    fun mtoast(text: String) {
        Toast.makeText(JobApp.instance.applicationContext, text, Toast.LENGTH_SHORT).show()
    }


    //replace frame layout to another fragment

    private fun loadFragment(fragment: Fragment) {
        val transaction = parentFragmentManager.beginTransaction()
        transaction.replace(R.id.JOb_board_frame_layout, fragment)
        transaction.disallowAddToBackStack()
        transaction.commit()
    }


    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun OnclickBannerImage(slideritem: SliderItem) {

    }

}