package com.dbvertex.job.network.response.discuss

import com.google.gson.annotations.SerializedName

data class DiscusAnswerDTO(

    @SerializedName("id")
    val id : String,

    @SerializedName("discuss_id")
    val discuss_id : String,

    @SerializedName("answer")
    val answer : String,

    @SerializedName("answered_by")
    val answered_by : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("sender_name")
    val sender_name : String,

    @SerializedName("profile_pic")
    val profile_pic : String,

    @SerializedName("sender_category")
    val sender_category : String,

    @SerializedName("answered")
    val answered : String,

    @SerializedName("favourite")
    val favourite : Boolean,

    @SerializedName("verified")
    val verified : Boolean,

    @SerializedName("Images")
    val imags : List<DDImage>,
)

