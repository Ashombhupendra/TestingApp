package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.resources.ResourcesDTO
import com.dbvertex.job.network.response.resources.ResourcesDetailDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface ResourcesServices {

    @GET("resources/resources")
    suspend fun getResources() : List<ResourcesDTO>

    @GET("resources/resources/{resource_id}")
    suspend fun getResourcesDetail(
       @Path("resource_id") resources_id : String
    ): ResourcesDetailDTO

}