package com.dbvertex.job.network.repository

import android.util.Log
import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.ManageJobServices
import com.dbvertex.job.network.utils.safelyCallApi

object ManageJobRepository {

    val managejobservices = getRetrofitService(ManageJobServices::class.java)

   // val userid = SharedPrefrenceHelper.user.userid.toString()

    suspend fun getAppliedJobs(userid:String) = safelyCallApi {
        Log.d("myuserid", userid)
        managejobservices.getAppliedJobs(userid)
    }

    suspend fun getPostedJobs(userid:String) = safelyCallApi {
        Log.d("myuserid", userid)
        managejobservices.getPostedJobs(userid)
    }
}