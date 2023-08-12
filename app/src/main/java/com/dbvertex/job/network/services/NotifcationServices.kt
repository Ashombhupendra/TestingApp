package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.notification.NotificationDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface NotifcationServices {

    @GET("webservice/getusernotification/{user_id}")
    suspend fun getNotifcation(
        @Path("user_id") user_id : String
    ): List<NotificationDTO>


    @GET("webservice/notificationcounter/{user_id}")
    suspend fun getNotificationCounter(
        @Path("user_id") user_id : String
    ) : String
}