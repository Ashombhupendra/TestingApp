package com.dbvertex.job.network.response.inspiration

import com.google.gson.annotations.SerializedName

data class InspirationImagesDTO (

    @SerializedName("image")
    val images : String
)