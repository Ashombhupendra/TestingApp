package com.dbvertex.job.network.response.jobboard

import com.google.gson.annotations.SerializedName

data class PostJobResponse(

    @SerializedName("status")
    val status : Boolean,

    @SerializedName("message")
    val message : String
)
