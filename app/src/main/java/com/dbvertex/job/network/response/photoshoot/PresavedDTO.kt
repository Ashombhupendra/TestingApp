package com.dbvertex.job.network.response.photoshoot

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PresavedDTO(
    @SerializedName("id")
    val id : String,

    @SerializedName("category_id")
    val category_id : String,

    @SerializedName("message")
    val message : String,

    @SerializedName("category_name")
    val category_name : String,

    @SerializedName("description")
    val description : String
): Parcelable
