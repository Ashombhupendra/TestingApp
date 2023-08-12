package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.FavoratesServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object FavoritesRepository {
    val favservices  = getRetrofitService(FavoratesServices::class.java)

    val user_id = SharedPrefrenceHelper.user.userid

    suspend fun getFavFreelancer() = safelyCallApi {
        favservices.getFavFreelancer(user_id.toString())
    }

    suspend fun getFavBlog() = safelyCallApi {
        favservices.getFavBlogs(user_id.toString())
    }

    suspend fun getFavDiscuss()= safelyCallApi {
        favservices.getFavDiscuss(user_id.toString())
    }



    suspend fun getFavEducation() = safelyCallApi {
        favservices.getFavEducation(user_id.toString())
    }

    suspend fun getFavInspiration() = safelyCallApi {
        favservices.getFavInspiration(user_id.toString())
    }
}