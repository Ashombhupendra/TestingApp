package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class EducationDetailDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("series_id")
    val series_id : String,

    @SerializedName("youtube_link")
    val youtube_link : String,

    @SerializedName("image")
    val image : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("subtitle")
    val subtitle : String,

    @SerializedName("description")
    val description : String,

    @SerializedName("choice")
    val choice : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("video_id")
    val video_id : String,

    @SerializedName("favourite")
    val favourite : Boolean
)
