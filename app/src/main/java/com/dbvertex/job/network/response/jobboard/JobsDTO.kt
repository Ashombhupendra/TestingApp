package com.dbvertex.job.network.response.jobboard

import com.google.gson.annotations.SerializedName

data class JobsDTO(
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

    @SerializedName("budget")
    val budget : String,

    @SerializedName("status")
    val status : String,

    @SerializedName("hired_user_id")
    val hired_user_id : String,

    @SerializedName("profile_pic")
    val profile_pic : String,

    @SerializedName("created")
    val created : String
)
