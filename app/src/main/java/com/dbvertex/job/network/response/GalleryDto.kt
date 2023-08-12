package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class GalleryDto(


    @SerializedName("Images")
    val images :  List<imagedto>
)
data class imagedto(

    @SerializedName("id")
    val imageid : String ,

    @SerializedName("image")
    val imageurl  : String
)