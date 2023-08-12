package com.dbvertex.job.network.response.ratingres

import com.google.gson.annotations.SerializedName

data class RatingDTO(
    @SerializedName("user_details")
    val userdetail : RatingdetailDTO,

    @SerializedName("reviews")
    val reviews : List<RecentReviewDTO>
)
