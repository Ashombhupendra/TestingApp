package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.jobboard.ApplyforJobDTO
import com.dbvertex.job.network.response.jobboard.JoberDetailDTO
import com.dbvertex.job.network.response.jobboard.JobsDTO
import com.dbvertex.job.network.response.jobboard.PostJobResponse
import com.google.gson.JsonObject
import retrofit2.http.*

interface JobBoardServices {

    // http://thephototribe.in/api/jobboard/job
    //{"status":false,"message":"Complete Parameteres"}
    //POST: sender_id, title, description, urgent, location, jobtype, budget, category, specialisation
    @POST("jobboard/job")
    @FormUrlEncoded
    suspend fun Postjob(
        @Field("sender_id") sender_id : String,
        @Field("title") title : String,
        @Field("description") description : String,
        @Field("urgent") urgent : String,
        @Field("location") location : String,
        @Field("jobtype") jobtype : String,
        @Field("budget") budget : String,
        @Field("category")category: String,
        @Field("specialisation")specialisation: String
    ) : PostJobResponse


    @POST("jobboard/job")
    @FormUrlEncoded
    suspend fun Updatejob(
        @Field("job_id") job_id : String,
        @Field("sender_id") sender_id : String,
        @Field("title") title : String,
        @Field("description") description : String,
        @Field("urgent") urgent : String,
        @Field("location") location : String,
        @Field("jobtype") jobtype : String,
        @Field("budget") budget : String,
        @Field("category")category: String,
        @Field("specialisation")specialisation: String?
    )


    //http://thephototribe.in/api/jobboard/jobs/{category_name}
    @POST("jobboard/jobs")
    @FormUrlEncoded
    suspend fun getjob(
        @Field("category")category_name :String
    ): List<JobsDTO>


    @POST("jobboard/searchjob")
    @FormUrlEncoded
    suspend fun searchjobs(
        @Field("searchString") searchString : String
    ): List<JobsDTO>

    @GET("jobboard/job/{job_id}/{user_id}")
    suspend fun getJobersDetail(
        @Path("job_id") job_id : String,
        @Path("user_id") user_id : String
    ): JoberDetailDTO


    @POST("jobboard/applyforjob")
    @FormUrlEncoded
    suspend fun Applyforjob(
        @Field("applicant_id") applicant_id : String,
        @Field("job_id")job_id : String
    ) : ApplyforJobDTO

    @GET("jobboard/deletejob/{job_id}")
    suspend fun deletejob(
        @Path("job_id") job_id: String
    ) : JsonObject
}