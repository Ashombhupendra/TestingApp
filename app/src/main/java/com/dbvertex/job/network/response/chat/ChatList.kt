package com.dbvertex.job.network.response.chat

import com.google.gson.annotations.SerializedName

data class ChatList(

    @SerializedName("id")
    val id : String,

    @SerializedName("sender_id")
    val sender_id : String,

    @SerializedName("receiver_id")
    val receiver_id : String,

    @SerializedName("content_type")
    val content_type : String,

    @SerializedName("content")
    val content : String,

    @SerializedName("read_device")
    val read_device : String,

    @SerializedName("read_status")
    val read_status : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("time")
    val time : String,

    @SerializedName("file_name")
    val file_name : String,

    @SerializedName("duration")
    val duration : Int
)
