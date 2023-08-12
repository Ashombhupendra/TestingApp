package com.dbvertex.job.peresentation.jobboard

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.JobRepository
import com.dbvertex.job.network.repository.ManageJobRepository
import com.dbvertex.job.network.response.jobboard.JobsDTO
import com.dbvertex.job.network.response.jobboard.jobsapplieduserlist
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.utils.SharedPrefrenceHelper
import kotlinx.coroutines.launch

class JobboardViewmodel : ViewModel() {

    //search result
    val jobslist = MutableLiveData<List<JobsDTO>>()


    val applieduser = MutableLiveData<List<jobsapplieduserlist>>()
    val postedjobs = MutableLiveData<List<jobsapplieduserlist>>()
    /**
     * Jobs detail
     *  {"status":true,"message":"Job Removed from Applied Successfully"}
     */
     val mtitle = MutableLiveData<String>()
    val mdescription = MutableLiveData<String>()
    val mbudget = MutableLiveData<String>()
    val mtype = MutableLiveData<String>()
    val musername = MutableLiveData<String>()
    val mlocation = MutableLiveData<String>()
    val mtime = MutableLiveData<String>()
    val mjobid = MutableLiveData<String>()
    val applyjob = MutableLiveData<Boolean>()
    val mCategory = MutableLiveData<String>()
    val mUrgent = MutableLiveData<String>()
    val mGuidelines = MutableLiveData<String>()
    val mSpecialisation = MutableLiveData<String>()
    val profile_pic = MutableLiveData<String>()
    val profile_id = MutableLiveData<String>()
    val profile_verified = MutableLiveData<Boolean>(false)

    val mypost = MutableLiveData<Boolean>(false)

    val managejob_post_counter  = MutableLiveData<String>()
    val managejob_apply_counter  = MutableLiveData<String>()

    val updateadapterdlt = MutableLiveData<Boolean>()



    fun deletejob(jobid: String)
    {
        viewModelScope.launch {
            val result = JobRepository.deletejob(jobid)
            when(result){
                is ResultWrapper.Success ->{

                   // temp_showToast("${result.response}")

                    updateadapterdlt.value = true
                }
                is ResultWrapper.Failure ->{

                  //  temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    fun mapplieduser(){

        viewModelScope.launch {

            val result = ManageJobRepository.getAppliedJobs(SharedPrefrenceHelper.user.userid.toString())

            when(result){
                is ResultWrapper.Success ->{
                    val lisst = mutableListOf<jobsapplieduserlist>()
                    lisst.addAll(result.response.jobs.map { it })

                    applieduser.value = lisst


                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }

        }
    }

    fun mPosteduser(){

        viewModelScope.launch {

            val result = ManageJobRepository.getPostedJobs(SharedPrefrenceHelper.user.userid.toString())

            when(result){
                is ResultWrapper.Success ->{
                    val lisst = mutableListOf<jobsapplieduserlist>()
                    lisst.addAll(result.response.jobs.map { it })
                    postedjobs.value = lisst

                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }

        }
    }



    fun applyforjob (userid:String){

        viewModelScope.launch {
                val result = JobRepository.ApplyForjob(mjobid.value.toString(),userid)
            when(result){
                is ResultWrapper.Success ->{

                    if (result.response.status) {
                        applyjob.value = result.response.message != "Job Removed from Applied Successfully"
                    }
                }
                is ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }
            }
        }

    }

    fun getjoberdetail(jobid : String) {
        viewModelScope.launch {
            val result = JobRepository.getJoberdetails(jobid)

                when (result) {
                    is ResultWrapper.Success -> {
                             mtitle.value = "${result.response.user.title}".capitalize()
                             mdescription.value= "${result.response.user.description}".capitalize()
                        mbudget.value = "Budget ${result.response.user.budget}"
                        mlocation.value = "${result.response.user.apilocation}"
                        mtime.value = "${result.response.user.created}"
                        mtype.value = "${result.response.user.jobtype}"
                        mjobid.value = result.response.user.job_id
                        applyjob.value = result.response.user.applied
                        profile_pic.value = result.response.user.profile_pic
                        profile_id.value = result.response.user.sender_id
                        profile_verified.value = result.response.user.verified
                        val userid = result.response.user.sender_id
                        val user_id = SharedPrefrenceHelper.user.userid
                        if (!user_id.toString().equals(userid)){
                            mypost.value = true
                        }else{
                            mypost.value = false
                        }
                        musername.value = "${result.response.user.firstname} ${result.response.user.last_name}".capitalize()


                    }
                    is ResultWrapper.Failure ->{
                         temp_showToast("${result.errorMessage}")
                    }
                }
        }
    }
    fun getAllJobs(categoryname  :String){
        viewModelScope.launch {
            val result = JobRepository.getjob(categoryname)
            when(result){
                is ResultWrapper.Success ->{

                    val list = mutableListOf<JobsDTO>()
                    list.addAll(result.response.map { it })
                    jobslist.value = list
                }
                is ResultWrapper.Failure ->{

                    temp_showToast("error"+result.errorMessage)

                }
            }

        }
    }

    fun searchjob(searchjob : String){
        viewModelScope.launch {
            val result = JobRepository.searchjob(searchjob)
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<JobsDTO>()
                    list.addAll(result.response.map { it })
                    jobslist.value = list
                }
                is ResultWrapper.Failure ->{
                    Log.d("error", result.errorMessage)

                }
            }
        }
    }
}