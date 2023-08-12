package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.EducationServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object EducationRepository {
    private val educationService = getRetrofitService(EducationServices::class.java)

    val userid = SharedPrefrenceHelper.user


    suspend fun getSeries(
        series_id: String
    ) = safelyCallApi {
        educationService.getSeries(user_id = userid.userid.toString(), series_id)
    }


    suspend fun getSingleContent(
        contentid  : String
    ) = safelyCallApi {
        educationService.getSingleContent(user_id = userid.userid.toString(), contentid = contentid)
    }

    suspend fun SetfavUnFav(
        contentid : String
    ) = safelyCallApi {
        educationService.SetFavUnFav(user_id = userid.userid.toString(), contentid)
    }

    suspend fun getEducationHighlights() = safelyCallApi {
        educationService.EducationHightlighContent()

    }

    suspend fun getSeriesContent() = safelyCallApi {
        educationService.getEducationSeries(SharedPrefrenceHelper.user.userid.toString())
    }
}