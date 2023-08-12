package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.InspirationServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object InspirationRepository {

    val inspirationservices = getRetrofitService(InspirationServices::class.java)
    val userid = SharedPrefrenceHelper.user.userid

    suspend fun getInspirationlist() = safelyCallApi {
        inspirationservices.getInspiration(userid.toString())
    }


    suspend fun getSingleInspiration(inspiration_id: String) = safelyCallApi {
        inspirationservices.getSingleInspiration(userid.toString() , inspiration_id)
    }


    suspend fun setlikeUnlike(
        inspiration_id : String
    ) = safelyCallApi {
        inspirationservices.InspirationlikeUnlike(userid.toString(), inspiration_id)
    }

    suspend fun setFavUnFav(
        inspiration_id : String
    ) = safelyCallApi {
        inspirationservices.InspirationFavUnFav(userid.toString(), inspiration_id)
    }

}