package com.dbvertex.job.data

import com.google.gson.annotations.SerializedName

data class SliderItem(

    @SerializedName("id")
    val id : String,

    @SerializedName("image")
    val ImageUrl : String,

    @SerializedName("referurl")
    val Intenturl : String,

    @SerializedName("updated")
    val updated : String


)
