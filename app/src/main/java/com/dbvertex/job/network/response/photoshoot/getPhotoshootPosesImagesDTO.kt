package com.dbvertex.job.network.response.photoshoot

import com.google.gson.annotations.SerializedName

data class getPhotoshootPosesImagesDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("photoshoot_id")
    val photoshoot_id : String,

    @SerializedName("pose_id")
    val pose_id : String,

    @SerializedName("image")
    var image : String
)
