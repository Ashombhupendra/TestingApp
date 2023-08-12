package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.NotifcationServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object NotificationRepository {
    val notificationservices = getRetrofitService(NotifcationServices::class.java)
    val user_id = SharedPrefrenceHelper.user.userid

    suspend fun getNotificationList() = safelyCallApi {
        notificationservices.getNotifcation(user_id.toString())
    }


    suspend fun getNotificationCounter() = safelyCallApi {
        notificationservices.getNotificationCounter(user_id.toString())
    }

}