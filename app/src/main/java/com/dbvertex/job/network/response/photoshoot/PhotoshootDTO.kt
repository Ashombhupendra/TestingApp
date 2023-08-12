package com.dbvertex.job.network.response.photoshoot

import com.google.gson.annotations.SerializedName

data class PhotoshootDTO(

    @SerializedName("id")
    val id : String,

    @SerializedName("user_id")
    val user_id : String,

    @SerializedName("title")
    val title : String,

    @SerializedName("session_id")
    val session_id : String,

    @SerializedName("photoshoot_time")
    val photoshoot_time : String,

    @SerializedName("address")
    val address : String,

    @SerializedName("my_goal")
    val my_goal : String,

    @SerializedName("note")
    val note : String,

    @SerializedName("client_name")
    val client_name : String,

    @SerializedName("project_delivered")
    val project_delivered : String,

    @SerializedName("contract_signed")
    val contract_signed : String,

    @SerializedName("received_full_payment")
    val received_full_payment : String,


    @SerializedName("created")
    val created : String



/*  "id": "2","user_id": "65","title": "I am title","session_id": "0","photoshoot_time": "2021-08-14 12:11:11",
"address": "Ujjain","note": "efeffg","client_name": "Alfas",
"my_goal": "To photoshoot 1 crore client with single camera.",
"contract_signed": "0",
"project_delivered": "0",
"received_full_payment": "0",
"created": "2021-08-14 18:55:43"*/
)
