package com.dbvertex.job.network.response

import com.dbvertex.job.data.RegisterDTO
import com.google.gson.annotations.SerializedName

data class RegistrationResponse (
    @SerializedName("status")
    val status : Boolean,
    @SerializedName("message")
    val message : String,
    @SerializedName("data")
    val data : RegisterDTO?
)