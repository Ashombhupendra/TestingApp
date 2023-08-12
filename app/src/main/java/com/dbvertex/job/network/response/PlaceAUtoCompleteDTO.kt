package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class PlaceAUtoCompleteDTO(
    @SerializedName("predictions")
    val prediction : List<placeList>
)
data class placeList(
    @SerializedName("description")
    val description : String
)