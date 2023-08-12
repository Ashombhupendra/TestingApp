package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class CompanyUserDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("user_company_user_id")
    val user_company_user_id : String,

    @SerializedName("user_company_name")
    val user_company_name : String,

    @SerializedName("user_company_address")
    val user_company_address : String,

    @SerializedName("user_company_specialisations")
    val user_company_specialisations : String,

    @SerializedName("user_company_experience")
    val user_company_experience : String,

    @SerializedName("user_company_socialid")
    val user_company_socialid : String,

    @SerializedName("user_company_assignment")
    val user_company_assignment : String,

    @SerializedName("user_company_videolinks")
    val user_company_videolinks : String,

    @SerializedName("user_company_about")
    val user_company_about : String,

    @SerializedName("user_company_created")
    val user_company_created : String

)
