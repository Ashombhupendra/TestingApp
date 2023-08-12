package com.dbvertex.job.network.response.jobboard

import com.google.gson.annotations.SerializedName

data class AppliedJobDTO(
    @SerializedName("total_applied")
    val totalapplied : String,

    val jobs : List<jobsapplieduserlist>
)


data class jobsapplieduserlist(

    @SerializedName("first_name")
    val firstname : String,

    @SerializedName("last_name")
    val last_name : String,

    @SerializedName("username")
    val username : String,

    @SerializedName("apilocation")
    val apilocation : String,

    @SerializedName("job_id")
    val job_id : String,

    @SerializedName("sender_id")
    val sender_id : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("specialisation")
    val specialisation : String,

    @SerializedName("category")
    val category : String,

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

    @SerializedName("profile_pic")
    val profile_pic : String,

    @SerializedName("created")
    val created : String
)
