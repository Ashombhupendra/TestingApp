package com.dbvertex.job.network.response

import com.dbvertex.job.data.EditorChoiceModel
import com.google.gson.annotations.SerializedName

data class EditerChoiceDto(

    @SerializedName("Photographer")
    val Photographer : List<EditorChoiceModel>?,

    @SerializedName("Drone")
    val Drone : List<EditorChoiceModel>?,

    @SerializedName("Retoucher")
    val Retoucher : List<EditorChoiceModel>?,

    @SerializedName("Cinematographer")
    val Cinematographer : List<EditorChoiceModel>?,

    @SerializedName("Shooting Studio/Location")
    val Shooting_Studio : List<EditorChoiceModel>?,

    @SerializedName("Rental Houses")
    val Rental_Houses : List<EditorChoiceModel>?,


    @SerializedName("Editor")
    val Editor : List<EditorChoiceModel>?
    //Editor
)
