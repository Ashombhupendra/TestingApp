package com.dbvertex.job.network.response.ratingres

import com.google.gson.annotations.SerializedName

data class RecentReviewDTO(
    @SerializedName("First_name")
    val First_name : String,

    @SerializedName("last_name")
    val last_name : String,

    @SerializedName("profile_pic")
    val profile_pic : String,

    @SerializedName("user_category")
    val user_category : String,

    @SerializedName("id")
    val id : String,

    @SerializedName("ratee_id")
    val ratee_id : String,

    @SerializedName("comment")
    val comment : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("average")
    val average : String
)
