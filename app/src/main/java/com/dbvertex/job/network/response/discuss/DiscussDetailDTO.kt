package com.dbvertex.job.network.response.discuss

import com.google.gson.annotations.SerializedName

data class DiscussDetailDTO(
    @SerializedName("question")
    val questions : AllDiscussionDTO,

    @SerializedName("Images")
    val imags : List<DDImage>,

    @SerializedName("answers")
    val answers : List<DiscusAnswerDTO>
)

data class DDImage(
    @SerializedName("image")
    val image : String
)
