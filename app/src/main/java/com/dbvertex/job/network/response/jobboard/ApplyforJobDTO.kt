package com.dbvertex.job.network.response.jobboard

import com.google.gson.annotations.SerializedName

data class ApplyforJobDTO(
    @SerializedName("status")
    val status : Boolean,

    @SerializedName("message")
    val message : String
)
