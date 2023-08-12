package com.dbvertex.job.network.response.photoshoot

import com.google.gson.annotations.SerializedName

//{"id":"169","category_id":"169","category_name":"Post Photoshoot","title":"Send client sneak peaks","status":"0"}
data class PhotoShootCheckListDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("category_id")
    val category_id : String,

    @SerializedName("category_name")
    val category_name : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("status")
    var status : Boolean

)
