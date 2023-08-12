package com.dbvertex.job.data

import com.google.gson.annotations.SerializedName

data class EditorChoiceModel(
    @SerializedName("user_id")
     val user_id : String,

    @SerializedName("username")
     val username : String,

    @SerializedName("first_name")
     val first_name : String,

    @SerializedName("last_name")
     val last_name : String,

    @SerializedName("profile_pic")
     val profile_pic : String?,

    @SerializedName("verified")
    val verified : Boolean
)
