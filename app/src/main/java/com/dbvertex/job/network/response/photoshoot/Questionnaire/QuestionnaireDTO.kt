package com.dbvertex.job.network.response.photoshoot.Questionnaire

import com.google.gson.annotations.SerializedName

data class QuestionnaireDTO(
    @SerializedName("id")
    val id : String,
    @SerializedName("question")
    val question : String,
    @SerializedName("type")
    val type : String,
)
/*
[
{
    "id": "2",
    "question": "Wha is yourhfasfdsafsffsfsafshf Name?",
    "type": "d"
},
{*/
