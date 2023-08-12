package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.PosesServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object PosesRepository {

    private val posesservices = getRetrofitService(PosesServices::class.java)

    private val user_id = SharedPrefrenceHelper.user.userid


    suspend fun getPosesCatetgory() = safelyCallApi {
        posesservices.getCategory()
    }

    suspend fun getSubcategory(category : String) = safelyCallApi {
        posesservices.getSubcategory(category)
    }

    suspend fun getPosesImages(subcategroy : String) = safelyCallApi {
         posesservices.getPOsesImages(subcategroy, user_id.toString())
    }

    suspend fun SetLikeUnlike(image_id : String) = safelyCallApi {
        posesservices.Poseslikeunlike(image_id, user_id.toString())
    }
}