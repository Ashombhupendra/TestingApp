package com.dbvertex.job.network.response.resources

import com.google.gson.annotations.SerializedName

data class ResourcesDTO(
    @SerializedName("resource_id")
    val resource_id : String,

    @SerializedName("backimage")
    val backimage : String,

    @SerializedName("name")
    val name : String
)
