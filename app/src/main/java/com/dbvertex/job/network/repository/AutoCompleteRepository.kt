package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getAutoRetrofitService
import com.dbvertex.job.network.services.AutoCompleteServices
import com.dbvertex.job.network.utils.safelyCallApi


object AutoCompleteRepository {


    private val autocomplete = getAutoRetrofitService(AutoCompleteServices::class.java)


    suspend fun getAutocompletelist(
        input : String
    ) = safelyCallApi {
          autocomplete.getPlacelist("AIzaSyAuJlJoxTMjjeSLvb3BYmty0eEkp6oHfCU", input)
    }

}