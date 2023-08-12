package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class EducationSingleSeriesContent(
    @SerializedName("series_name")
    val series_name : String,

    @SerializedName("series_id")
    val series_id : String,

    @SerializedName("data")
    val data : List<SingleSerieslist>,
)


data class SingleSerieslist(
    @SerializedName("content_id")
    val content_id : String,

    @SerializedName("series_name")
    val series_name : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("subtitle")
    val subtitle : String,

    @SerializedName("image")
    val image : String,

    @SerializedName("series_id")
    val series_id : String,

    @SerializedName("favourite")
    val favourite : Boolean



)
