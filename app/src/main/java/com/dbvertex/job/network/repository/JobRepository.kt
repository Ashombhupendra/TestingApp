package com.dbvertex.job.network.repository

import android.util.Log
import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.JobBoardServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object JobRepository {

   private val jobService = getRetrofitService(JobBoardServices::class.java)

    val userid = SharedPrefrenceHelper.user.userid
    //POST: sender_id, title, description, urgent, location, jobtype, budgetcategory, specialisation


    suspend fun deletejob(job_id :String) = safelyCallApi {
        jobService.deletejob(job_id)
    }

    suspend fun Postjob(
        title : String,
        description : String,
        urgent : String,
        location : String,
        jobtype : String,
        budget : String,
        category : String,
        specialisation : String,
        userid: String
    ) = safelyCallApi {
        jobService.Postjob(userid, title, description, urgent, location, jobtype, budget, category, specialisation)
    }
    suspend fun Updatejob(
        job_id : String,
        title : String,
        description : String,
        urgent : String,
        location : String,
        jobtype : String,
        budget : String,
        category : String,
        specialisation : String
    ) = safelyCallApi {
        jobService.Updatejob(job_id, userid.toString(), title, description, urgent, location, jobtype, budget, category, specialisation)
    }

    suspend fun searchjob(searchjob : String) = safelyCallApi {
        jobService.searchjobs(searchjob)
    }

    suspend fun ApplyForjob(jobid: String,userid:String) = safelyCallApi {
        Log.d("applyby",userid);
        jobService.Applyforjob(userid, jobid)
    }

    suspend fun getjob(categoryname : String) = safelyCallApi {
        jobService.getjob(categoryname)

    }

    suspend fun getJoberdetails(jobid : String) = safelyCallApi {
        jobService.getJobersDetail(jobid, userid.toString())
    }
}