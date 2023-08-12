package com.dbvertex.job.peresentation.jobboard.postjob

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.JobRepository
import com.dbvertex.job.network.response.jobboard.jobsapplieduserlist
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.auth.NetworkState
import kotlinx.coroutines.launch

class PostJobViewmodel : ViewModel() {

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
    val mUrgent = MutableLiveData<String>("1")
    val mSpecialisation = MutableLiveData<String>()
    val JobId = MutableLiveData<String>()
    val postjobstate = MutableLiveData<NetworkState>()


    fun Postjob(userid:String) {

        postjobstate.value = NetworkState.LOADING_STARTED
        postjobstate.value = NetworkState.FAILED
        viewModelScope.launch {
            postjobstate.value = NetworkState.LOADING_STOPPED
            val result = JobRepository.Postjob(
                mtitle.value.toString(),
                mdescription.value.toString(),
                mUrgent.value.toString(),
                mlocation.value.toString(),
                mtype.value.toString(),
                mbudget.value.toString(),
                mCategory.value.toString(),
                "test",
                //mSpecialisation.value.toString()
            userid
            )
            when (result) {
            is ResultWrapper.Success -> {
                postjobstate.value = NetworkState.SUCCESS

                /// temp_showToast("${result.response}")
            }
            is ResultWrapper.Failure -> {
               Log.d("post_job_response",result.errorMessage)
                postjobstate.value = NetworkState.FAILED

             //   temp_showToast("${result.errorMessage}")
            }
        }
        }
    }

    fun Updatejob() {

        postjobstate.value = NetworkState.LOADING_STARTED
        viewModelScope.launch {
            postjobstate.value = NetworkState.LOADING_STOPPED

            val result = JobRepository.Updatejob(
                JobId.value.toString(),
                mtitle.value.toString(),
                mdescription.value.toString(),
                mUrgent.value.toString(),
                mlocation.value.toString(),
                mtype.value.toString(),
                mbudget.value.toString(),
                mCategory.value.toString(),
                mSpecialisation.value.toString()
            )

            when (result) {
                is ResultWrapper.Success -> {
                    postjobstate.value = NetworkState.SUCCESS

                    // temp_showToast("${result.response}")
                }
                is ResultWrapper.Failure -> {
                    postjobstate.value = NetworkState.FAILED

                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }

    fun setdefaultvalue(appliedJobDTO: jobsapplieduserlist) {

        JobId.value = appliedJobDTO.job_id
        mtitle.value = appliedJobDTO.title
        mdescription.value = appliedJobDTO.description
        mCategory.value = appliedJobDTO.category
        mSpecialisation.value = appliedJobDTO.specialisation
        mlocation.value = appliedJobDTO.location
        mtype.value = appliedJobDTO.jobtype
        mbudget.value = appliedJobDTO.budget
        mUrgent.value = appliedJobDTO.urgent

    }
}