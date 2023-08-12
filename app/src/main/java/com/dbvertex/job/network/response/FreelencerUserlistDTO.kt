package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class FreelencerUserlistDTO(

    @SerializedName("user_id")
    val user_id : String,

    @SerializedName("first_name")
    val first_name : String,
    @SerializedName("last_name")
    val last_name : String,

    @SerializedName("user_freelancer_category")
    val user_freelancer_category : String,

    @SerializedName("ratings")
    val ratings : String,

    @SerializedName("ratings_count")
    val ratings_count : String,


    @SerializedName("profile_pic")
    val profile_img : String,

    @SerializedName("verified")
    val verified : Boolean



)
