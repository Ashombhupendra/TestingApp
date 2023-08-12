package com.dbvertex.job.network.response.poses

import com.google.gson.annotations.SerializedName

data class PosesImagesDTO(

    @SerializedName("id")
    val id : String,

    @SerializedName("subcategory_id")
    val subcategory_id : String,

    @SerializedName("image")
    val image : String,

    @SerializedName("liked")
    val liked : Boolean
)
