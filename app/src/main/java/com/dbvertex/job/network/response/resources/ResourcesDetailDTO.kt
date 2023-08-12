package com.dbvertex.job.network.response.resources

import com.google.gson.annotations.SerializedName

data class ResourcesDetailDTO(
    @SerializedName("resource_id")
    val resource_id : String,

    @SerializedName("backimage")
    val backimage : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("name")
    val name : String,

    @SerializedName("description")
    val description : String,

    @SerializedName("images")
    val images : List<ResourcesImageList>

)

data class ResourcesImageList(
    @SerializedName("id")
    val id : String,

    @SerializedName("resource_id")
    val resource_id : String,

    @SerializedName("image")
    val image : String,
)
