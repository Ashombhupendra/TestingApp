package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.AboutUsServices
import com.dbvertex.job.network.utils.safelyCallApi

object AboutUsRepository {
    private val service = getRetrofitService(AboutUsServices::class.java)

    suspend fun getAboutUs(): String {
        val res = service.getAboutUs()
        return res
    }

    suspend fun getPrivacyPolicy(): String {
        val res = service.getPrivacyPolicy()
        return res
    }

    suspend fun getTermsAndConditions(): String {
        val res = service.getTermsAndCondition()
        return res
    }

    suspend fun postContactUs(fullname : String, email : String, feedback : String) = safelyCallApi {
        service.postContactUs(fullname, email, feedback)
    }

    suspend fun getHeadlines() = safelyCallApi {
        service.getNewsheadline()
    }
}