package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class EducationSeriesContent(

    @SerializedName("series_name")
    val series_name : String,

    @SerializedName("series_id")
    val series_id : String,

    @SerializedName("data")
    val data : List<Serieslist>,
)


data class Serieslist(
    @SerializedName("content_id")
    val content_id : String,

    @SerializedName("id")
    val id : String,

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
