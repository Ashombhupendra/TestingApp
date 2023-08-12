package com.dbvertex.job.network.response.photoshoot

import com.google.gson.annotations.SerializedName

data class PhotoshootTimelineDTO(
    @SerializedName("id")
    val id : String,
    @SerializedName("photoshoot_id")
    val photoshoot_id : String,
    @SerializedName("title")
    val title : String,
    @SerializedName("time")
    val time : String,
    @SerializedName("status")
    var status : Boolean,

)
