package com.dbvertex.job.network.response.discuss

import com.google.gson.annotations.SerializedName


data class AddQuestionResponse(

    //{"status":true,"message":"Discussion Added Successfully."}
    @SerializedName("status")
    val status : Boolean,

    @SerializedName("message")
    val message : String
)
