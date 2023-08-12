package com.dbvertex.job.network.response

import com.google.gson.annotations.SerializedName

data class FreelancerAboutmeProRes(
   @SerializedName("user_id")
    val user_id : String,
   @SerializedName("user_freelancer_aboutme")
   val Aboutme   : String,
   @SerializedName("passport")
   val passport : Int,
   @SerializedName("manuallocation")
   val Manual_Location : String,
   @SerializedName("apilocation")
   val API_Locaion : String,

   @SerializedName("user_other_review")
    val user_other_review : String,

   @SerializedName("user_category")
   val user_category : String,
   @SerializedName("user_company_about")
   val user_company_about : String
)
