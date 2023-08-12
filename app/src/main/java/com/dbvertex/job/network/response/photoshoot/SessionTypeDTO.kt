package com.dbvertex.job.network.response.photoshoot

import com.google.gson.annotations.SerializedName

data class SessionTypeDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("created")
    val created : String
)
