package com.dbvertex.job.network.response.poses

import com.google.gson.annotations.SerializedName

data class SubcategoryDTO(

    @SerializedName("id")
    val id : String,

    @SerializedName("pose_id")
    val pose_id : String,

    @SerializedName("name")
    val name : String
)
