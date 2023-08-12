package com.dbvertex.job.peresentation.jobboard.findjob

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentFindJobBinding
import com.dbvertex.job.network.response.jobboard.JobsDTO
import com.dbvertex.job.network.response.poses.SubcategoryDTO
import com.dbvertex.job.peresentation.jobboard.JobBoardDetail
import com.dbvertex.job.peresentation.jobboard.JobboardViewmodel
import com.dbvertex.job.peresentation.poses.PosesSubcategoryAdapter
import com.dbvertex.job.peresentation.poses.onPosesSubcategoryClick
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.android.material.transition.MaterialFadeThrough

class FindJobFrag : Fragment(), onJobClick, onPosesSubcategoryClick {

    private lateinit var mBinding : FragmentFindJobBinding
    val categorylist = ArrayList<SubcategoryDTO>()
    var editenable = false
    private  val mJobviewmodle   by activityViewModels<JobboardViewmodel>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentFindJobBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@FindJobFrag

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        categorylist.add(SubcategoryDTO("", "", "All"))
        categorylist.add(SubcategoryDTO("", "", "Administrative Assistant"))
        categorylist.add(SubcategoryDTO("", "", "Executive Assistant"))
        categorylist.add(SubcategoryDTO("", "", "Marketing Manager"))
        categorylist.add(SubcategoryDTO("", "", "Customer Service Representative"))
        categorylist.add(SubcategoryDTO("", "", "Nurse Practitioner"))
        categorylist.add(SubcategoryDTO("", "", "Software Engineer"))
        categorylist.add(SubcategoryDTO("", "", "Sales Manager"))
        categorylist.add(SubcategoryDTO("", "", "Data Entry Clerk"))
        categorylist.add(SubcategoryDTO("", "", "Real Estate"))
        categorylist.add(SubcategoryDTO("", "", "Video Editing"))
        categorylist.add(SubcategoryDTO("", "", "Office Assistant"))
        categorylist.add(SubcategoryDTO("", "", "Internship"))

        val adapter = PosesSubcategoryAdapter(JobApp.instance.applicationContext, categorylist , this)
        mBinding.fjCategoryContainer.adapter = adapter
        mJobviewmodle.getAllJobs("")

        mBinding.fjSearch.doAfterTextChanged {
            if (it.isNullOrEmpty()){

            }else{
                mJobviewmodle.searchjob(it.toString())
                editenable = true

            }
        }

        //search result
        mJobviewmodle.jobslist.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                mBinding.noPosesImages.visibility = View.VISIBLE
                var final_list=it.filter { it.sender_id!=SharedPrefrenceHelper.user.userid.toString() }
                val adapter = AllJobAdapter(JobApp.instance.applicationContext, final_list as ArrayList<JobsDTO>,this)
                mBinding.fjRecyclerview.adapter = adapter
                //response profile pic
            //https://dbvertex.com/thephototribe/uploads/profile/9703.jpg
                //http://thephototribe.in/uploads/profile/9703.jpg
            }
            else  {
                var final_list=it.filter { it.sender_id!=SharedPrefrenceHelper.user.userid.toString() }
                val adapter = AllJobAdapter(JobApp.instance.applicationContext, final_list as ArrayList<JobsDTO>,this)
                mBinding.fjRecyclerview.adapter = adapter
                mBinding.fjRecyclerview.setHasFixedSize(true)
                mBinding.noPosesImages.visibility = View.GONE
            }


        })
    }

    override fun onNavigateJobDetail(jobsdto: JobsDTO) {
                mJobviewmodle.getjoberdetail(jobsdto.job_id)
        Log.d("newflow",jobsdto.job_id.toString())
        Log.d("newflow",jobsdto.sender_id.toString())

        JobBoardDetail.JobUserId.value=jobsdto.sender_id.toString()
                findNavController().navigate(R.id.jobBoardDetail)
    }

    override fun onSubcategoryclick(subcategoryDTO: SubcategoryDTO) {

        if (editenable){
            mBinding.fjSearch.setText("")
            editenable = false
        }
             if (subcategoryDTO.name.equals("All")){
                 mJobviewmodle.getAllJobs("")
             }
             else{
                 mJobviewmodle.getAllJobs(subcategoryDTO.name)
             }
    }

}