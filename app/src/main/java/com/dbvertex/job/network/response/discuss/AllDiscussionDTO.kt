package com.dbvertex.job.network.response.discuss

import com.google.gson.annotations.SerializedName

data class AllDiscussionDTO(

    @SerializedName("question_id")
    val questionid : String,

    @SerializedName("question")
    val question : String,

    @SerializedName("description")
    val description : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("sender_id")
    val sender_id : String,

    @SerializedName("sender_name")
    val sender_name : String,

    @SerializedName("profile_pic")
    val profile_pic : String,

    @SerializedName("sender_category")
    val sender_category : String,


    @SerializedName("total_answers")
    val total_answers : String,

    @SerializedName("last_answered")
    val last_answered : String,

    @SerializedName("favourite")
    var favourite : Boolean,

    @SerializedName("verified")
    val verified : Boolean

    )
