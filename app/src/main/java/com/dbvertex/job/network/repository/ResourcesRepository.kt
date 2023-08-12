package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.ResourcesServices
import com.dbvertex.job.network.utils.safelyCallApi

object ResourcesRepository {

    private val resourcesServices = getRetrofitService(ResourcesServices::class.java)


    suspend fun getResourcesList() = safelyCallApi {
        resourcesServices.getResources()
    }

    suspend fun getResourcesDetail(
        resource_id : String
    ) = safelyCallApi {
        resourcesServices.getResourcesDetail(resource_id)
    }
}