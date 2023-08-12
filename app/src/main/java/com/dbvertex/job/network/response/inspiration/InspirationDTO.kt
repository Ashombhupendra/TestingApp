package com.dbvertex.job.network.response.inspiration

import com.google.gson.annotations.SerializedName

data class InspirationDTO(

    @SerializedName("id")
    val id : String,

    @SerializedName("body")
    val body : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("total_likes")
    val total_likes : String,

    @SerializedName("user_liked")
    var user_liked : Boolean,

    @SerializedName("user_saved")
    var user_saved : Boolean,

    @SerializedName("images")
    val images : List<InspirationImagesDTO>,
)
