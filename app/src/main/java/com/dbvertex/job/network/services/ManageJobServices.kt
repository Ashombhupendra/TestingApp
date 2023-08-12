package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.jobboard.AppliedJobDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ManageJobServices {

    @GET("jobboard/appliedjobs/{user_id}")
    suspend fun getAppliedJobs(
        @Path("user_id") user_id: String
    ): AppliedJobDTO

    @GET("jobboard/postedjobs/{user_id}")
    suspend fun getPostedJobs(
        @Path("user_id") user_id: String
    ): AppliedJobDTO
}