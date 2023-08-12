package com.dbvertex.job.network.response.photoshoot

import com.google.gson.annotations.SerializedName

data class ImagePhotoshootlistDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("photoshoot_time")
    val photoshoot_time : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("image_added")
    var isAdded : Boolean
)
