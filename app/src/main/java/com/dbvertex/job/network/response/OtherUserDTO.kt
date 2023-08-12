package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class OtherUserDTO(
    @SerializedName("user_other_user_id")
    val user_other_user_id : String,

    @SerializedName("user_other_specialisation")
    val user_other_specialisation : String,

    @SerializedName("user_other_sociallink")
    val user_other_sociallink : String,

    @SerializedName("user_other_review")
    val user_other_review : String,

    @SerializedName("user_other_status")
    val user_other_status : String,

    @SerializedName("user_other_loginstatus")
    val user_other_loginstatus : String,

    @SerializedName("user_other_videolinks")
    val user_other_videolinks : String
)
