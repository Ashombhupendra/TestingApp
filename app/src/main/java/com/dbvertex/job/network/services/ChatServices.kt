package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.chat.ChatList
import com.dbvertex.job.network.response.chat.ChatuserDTO
import okhttp3.MultipartBody
import retrofit2.http.*

interface ChatServices {


    @GET("chat/getchatlist/{user_id}")
    suspend fun getChatUser(
        @Path("user_id") user_id : String

    ): List<ChatuserDTO>


    @GET("chat/getpdfurl/{user_id}")
    suspend fun  getPDFnavigate(
        @Path("user_id") user_id : String
    )  : String

    @GET("chat/getchats/{user1}/{user2}")
    suspend fun getChatList(
        @Path("user1") user1 : String,
        @Path("user2") user2 : String
    ): List<ChatList>

    @POST("chat/chat")
    @FormUrlEncoded
    suspend fun sendtextmessage(
        @Field("sender_id") sender_id :String,
        @Field("receiver_id") receiver_id :String,
        @Field("message") message :String
    )

    @POST("chat/chatfile")
    @Multipart
    suspend fun sendMultimediamsg(
        @Part sender_id: MultipartBody.Part,
        @Part receiver_id: MultipartBody.Part,
        @Part file : MultipartBody.Part
    )
}