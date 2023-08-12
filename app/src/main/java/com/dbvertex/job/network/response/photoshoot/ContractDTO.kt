package com.dbvertex.job.network.response.photoshoot

import com.google.gson.annotations.SerializedName

data class ContractDTO (
    @SerializedName("id")
    val id : String,
    @SerializedName("contract_code")
    val contract_code : String,
    @SerializedName("content")
    val content : String,
)