package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class EqModelsdto(
    @SerializedName("id")
    val id: String,

    @SerializedName("equipment_id")
    val equipment_id: String,

    @SerializedName("Company_id")
    val Company_id: String,

    @SerializedName("model_name")
    val model_name: String
)