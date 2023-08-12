package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.EducationDetailDTO
import com.dbvertex.job.network.response.EducationHighlightDTO
import com.dbvertex.job.network.response.EducationSeriesContent
import com.dbvertex.job.network.response.EducationSingleSeriesContent
import retrofit2.http.*

interface EducationServices {

     //http://thephototribe.in/api/education/setfavourite

    @GET("education/getseries/{user_id}/{series_id}")
    suspend fun getSeries(
        @Path("user_id") user_id : String,
        @Path("series_id") series_id : String
    ) : EducationSingleSeriesContent

    @GET("education/getcontent/{user_id}/{content_id}")
    suspend fun getSingleContent(
        @Path("user_id") user_id : String,
        @Path("content_id") contentid : String
    ): EducationDetailDTO

    @POST("education/setfavourite")
    @FormUrlEncoded
    suspend fun SetFavUnFav(
        @Field("user_id") user_id : String,
        @Field("content_id") content_id : String
    )


    @GET("education/gethighlightseries")
    suspend fun EducationHightlighContent() : List<EducationHighlightDTO>


    @GET("education/getseries/{user_id}")
    suspend fun getEducationSeries(
        @Path("user_id") user_id : String
    ) : List<EducationSeriesContent>
}