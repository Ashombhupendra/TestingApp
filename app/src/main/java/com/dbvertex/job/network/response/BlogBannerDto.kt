package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class BlogBannerDto(

    @SerializedName("id")
    val blogid : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("description")
    val description : String,

    @SerializedName("choice")
    val choice : String,

    @SerializedName("created")
    val created : String,

    @SerializedName("link")
    val link : String,

    @SerializedName("image")
    val image : String,

     @SerializedName("favorite")
     var favorite : Boolean
    //image

)
