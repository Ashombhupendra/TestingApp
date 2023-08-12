package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class EducationHighlightDTO(
    @SerializedName("content_id")
    val content_id : String,
    @SerializedName("title")
    val title : String,
    @SerializedName("subtitle")
    val subtitle : String,
    @SerializedName("image")
    val image : String,
    @SerializedName("series_id")
    val series_id : String
)
