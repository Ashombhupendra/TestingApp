package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class PodcastDTO(
    @SerializedName("id")
    val id : String,
    @SerializedName("title")
    val title : String,
    @SerializedName("description")
    val description : String,
    @SerializedName("created")
    val created : String,
    @SerializedName("audio")
    val audio : String,
    @SerializedName("duration")
    val duration : Int,
    @SerializedName("favorite")
    var favourite : Boolean
)
