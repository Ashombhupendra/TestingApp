package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class AuthResponse(

    @SerializedName("status")
    val status : Boolean,

    @SerializedName("otp")
    val otp : String,

    @SerializedName("data")
    val data : userdata?
)


data class userdata(
    @SerializedName("user_id")
    val user_id : String,

    @SerializedName("username")
    val username : String,

    @SerializedName("first_name")
    val first_name : String,

    @SerializedName("last_name")
    val last_name : String,

    @SerializedName("profile_pic")
    val profile_pic : String,

    @SerializedName("profile_back")
    val profile_back : String,


    @SerializedName("phone")
    val phone : String,

    @SerializedName("dob")
    val dob : String,

    @SerializedName("user_category")
    val user_category : String,

    @SerializedName("status")
    val status : String,

    @SerializedName("login_status")
    val login_status : String,

    @SerializedName("manuallocation")
    val manuallocation : String,

    @SerializedName("apilocation")
    val apilocation : String,

    @SerializedName("profile_status")
    val profile_status : Boolean,

    @SerializedName("active_status")
    val active_status : Boolean,

    @SerializedName("verified")
    val verified : String
)
