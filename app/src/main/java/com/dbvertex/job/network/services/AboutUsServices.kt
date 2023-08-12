package com.dbvertex.job.network.services

import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AboutUsServices {

/*    http://thephototribe.in/api/webservice/aboutus
    http://thephototribe.in/api/webservice/termsnconditions
    http://thephototribe.in/api/webservice/privacypolicy*/

    @GET("webservice/aboutus")
    suspend fun getAboutUs(): String

    @GET("webservice/privacypolicy")
    suspend fun getPrivacyPolicy(): String

    @GET("webservice/termsnconditions")
    suspend fun getTermsAndCondition(): String

    @POST("contact/contact")
    @FormUrlEncoded
    suspend fun postContactUs(
         @Field("name") full_name : String,
         @Field("email") email : String,
         @Field("feedback") feedback : String
    ): Boolean

    @GET("webservice/news")
    suspend fun getNewsheadline(): String

}