package com.dbvertex.job.network.response.chat

import com.google.gson.annotations.SerializedName

data class ChatuserDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("sender_id")
    val sender_id : String,

    @SerializedName("receiver_id")
    val receiver_id : String,

    @SerializedName("message")
    val message : String,

    @SerializedName("content_type")
    val content_type : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("profile_pic")
    val profile_pic : String,

    @SerializedName("unread")
    val unread : String,

    @SerializedName("first_name")
    val first_name : String,


    @SerializedName("last_name")
    val last_name : String,

    @SerializedName("verified")
    val verified : Boolean


)
