package com.dbvertex.job.network.response.photoshoot

import com.google.gson.annotations.SerializedName

data class Upcoming_complete_Photoshoot_DTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("photoshoot_time")
    val photoshoot_time : String,

    @SerializedName("title")
    val title : String,
)
