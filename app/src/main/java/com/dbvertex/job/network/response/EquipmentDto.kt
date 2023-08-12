package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class  EquipmentDto(

    @SerializedName("id")
    val id : String,

    @SerializedName("name")
    val name : String
)
