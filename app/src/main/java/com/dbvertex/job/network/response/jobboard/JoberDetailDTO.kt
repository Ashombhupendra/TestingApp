package com.dbvertex.job.network.response.jobboard

import com.google.gson.annotations.SerializedName

data class JoberDetailDTO(

    @SerializedName("job_details")
    val user : joberdto
)

data class joberdto(
    @SerializedName("first_name")
    val firstname : String,

    @SerializedName("last_name")
    val last_name : String,

    @SerializedName("apilocation")
    val apilocation : String,

    @SerializedName("job_id")
    val job_id : String,

    @SerializedName("sender_id")
    val sender_id : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("description")
    val description : String,

    @SerializedName("urgent")
    val urgent : String,

    @SerializedName("location")
    val location : String,

    @SerializedName("jobtype")
    val jobtype : String,

    @SerializedName("specialisation")
    val specialisation : String,

    @SerializedName("budget")
    val budget : String,

    @SerializedName("status")
    val status : String,

    @SerializedName("hired_user_id")
    val hired_user_id : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("ratings")
    val ratings : String,

    @SerializedName("profile_pic")
    val profile_pic : String,


    @SerializedName("total_ratings")
    val total_ratings : String,

    @SerializedName("applied")
    val applied : Boolean,

    @SerializedName("verified")
    val verified : Boolean

)
