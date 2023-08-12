package com.dbvertex.job.data

import com.google.gson.annotations.SerializedName

data class RegisterDTO (
    @SerializedName("user_id")
    val id: String,
    @SerializedName("first_name")
    val first_name: String,

    @SerializedName("last_name")
    val last_name: String,
    @SerializedName("phone")
    val phone: String,
    @SerializedName("status")
    val status: String,
    @SerializedName("user_category")
    val user_category: String,

    @SerializedName("profile_pic")
    val profile_pic : String

    )