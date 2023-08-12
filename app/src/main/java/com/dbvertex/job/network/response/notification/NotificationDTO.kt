package com.dbvertex.job.network.response.notification

import com.google.gson.annotations.SerializedName

data class NotificationDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("user_id")
    val user_id : String,

    @SerializedName("message")
    val message : String,

    @SerializedName("content_id")
    val content_id : String,

    @SerializedName("content_type")
    val content_type : String,

    @SerializedName("read_status")
    val read_status : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("user_profile")
    val user_profile : String

)

