package com.dbvertex.job.network.response.poses

import com.google.gson.annotations.SerializedName

data class CategoryDTO(

    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("img")
    val img : String

    )
