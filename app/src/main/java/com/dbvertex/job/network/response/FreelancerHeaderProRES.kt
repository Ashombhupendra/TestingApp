package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class FreelancerHeaderProRES(
    @SerializedName("user_id")
    val user_id : String,
    @SerializedName("username")
    val username   : String,
    @SerializedName("first_name")
    val first_name : String,
    @SerializedName("last_name")
    val last_name : String,
    @SerializedName("category")
    val category : String,

    @SerializedName("profile_pic")
    val profile_pic : String,

    @SerializedName("profile_back")
    val profile_back : String,

    @SerializedName("favorite")
    val favorite : Boolean,

    @SerializedName("verified")
    val verified : Boolean
)
