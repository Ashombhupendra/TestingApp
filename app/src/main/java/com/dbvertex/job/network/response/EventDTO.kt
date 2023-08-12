package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class EventDTO(
    @SerializedName("user_id")
    val user_id  : String,

    @SerializedName("from_date")
    val from_date : String,

    @SerializedName("to_date")
    val to_date : String,

    @SerializedName("event")
    val event : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("status")
    val status : String
)
