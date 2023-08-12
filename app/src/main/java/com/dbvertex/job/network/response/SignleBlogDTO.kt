package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class SignleBlogDTO(

    @SerializedName("id")
    val id : String ,

    @SerializedName("title")
    val title : String ,

    @SerializedName("description")
    val description : String ,

    @SerializedName("created")
    val created : String ,

    @SerializedName("favorite")
    val favorite : Boolean ,

    @SerializedName("image")
    val image : List<blogdetailImageDTO>

)

  public  data class blogdetailImageDTO(
    @SerializedName("image")
    val image: String
)
