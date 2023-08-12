package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.PlaceAUtoCompleteDTO
import retrofit2.http.*

interface AutoCompleteServices {


    @POST("place/autocomplete/json?")
    suspend fun getPlacelist(
        @Query("key") key : String,
        @Query("input") input : String) : PlaceAUtoCompleteDTO
}