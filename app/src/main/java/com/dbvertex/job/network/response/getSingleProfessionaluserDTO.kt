package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class getSingleProfessionaluserDTO(

    @SerializedName("user_freelancer_user_id")
    val user_freelancer_user_id : String,

    @SerializedName("user_freelancer_category")
    val user_freelancer_category : String,

    @SerializedName("user_freelancer_specialisation")
    val user_freelancer_specialisation : String,

    @SerializedName("user_freelancer_experience")
    val user_freelancer_experience : String,

    @SerializedName("user_freelancer_budget")
    val user_freelancer_budget : String,

    @SerializedName("passport")
    val passport : String,

    @SerializedName("user_freelancer_aboutme")
    val user_freelancer_aboutme : String,

    @SerializedName("user_freelancer_videolinks")
    val user_freelancer_videolinks : String,

    @SerializedName("user_freelancer_calenderstatus")
    val user_freelancer_calenderstatus : String,

    @SerializedName("user_freelancer_choice")
    val user_freelancer_choice : String,

    val user_freelancer_equipments : List<getSingleEquipments>
)

data class getSingleEquipments(
    @SerializedName("equipmentModel")
    val equipmentModel : String,

    @SerializedName("equipmentcompany")
    val equipmentcompany : String,

    @SerializedName("equipmentname")
    val equipmentname : String,
)
