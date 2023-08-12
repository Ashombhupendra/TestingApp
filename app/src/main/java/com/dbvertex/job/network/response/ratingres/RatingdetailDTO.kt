package com.dbvertex.job.network.response.ratingres

import com.google.gson.annotations.SerializedName

data class RatingdetailDTO(

    @SerializedName("WorkReviews")
    val WorkReviews : String,

    @SerializedName("num_ratings")
    val num_ratings : String,

    @SerializedName("creativity")
    val creativity : String,

    @SerializedName("punctuality")
    val punctuality : String,

    @SerializedName("communication")
    val communication : String,

    @SerializedName("presentation")
    val presentation : String,

    @SerializedName("workEthics")
    val workEthics : String
)
