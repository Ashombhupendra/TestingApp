package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class ManufatureDto(

    @SerializedName("id")
    val id : String,

    @SerializedName("equipment_id")
    val equipment_id : String,
    @SerializedName("company_name")
    val company_name : String


)
