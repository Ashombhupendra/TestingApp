package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.inspiration.InspirationDTO
import retrofit2.http.*

interface InspirationServices {



    @GET("inspirational/inspirationals/{user_id}/{inspirational_id}")
    suspend fun getSingleInspiration(
        @Path("user_id") user_id: String,
        @Path("inspirational_id") inspirational_id : String
    ) : InspirationDTO



    @GET("inspirational/inspirationals/{user_id}")
    suspend fun getInspiration(
        @Path("user_id") user_id : String
    ) : List<InspirationDTO>


    @POST("inspirational/setlikeunlike")
    @FormUrlEncoded
    suspend fun InspirationlikeUnlike(
        @Field("user_id") user_id: String,
        @Field("inspirational_id") inspirational_id : String

    )


    @POST("inspirational/favourite")
    @FormUrlEncoded
    suspend fun InspirationFavUnFav(
        @Field("user_id") user_id: String,
        @Field("inspirational_id") inspirational_id : String

    )
}