package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.PodcastDTO
import retrofit2.http.*

interface PodcastServices {


  /*  http://thephototribe.in/api/webservice/addtofavpodcast
    POST:podcast_id, user_id*/

    @GET("webservice/podcasts/{user_id}")
    suspend fun getAllpodcast(
        @Path("user_id") user_id  : String
    ): List<PodcastDTO>

    @GET("favourites/podcast/{user_id}")
    suspend fun getFavPodcast(
        @Path("user_id") user_id : String
    ): List<PodcastDTO>

    //http://thephototribe.in/api/webservice/podcastsearch POST : keyword, user_id
    @POST("webservice/podcastsearch")
    @FormUrlEncoded
     suspend fun searchPodcast(
        @Field("user_id")user_id : String,
        @Field("keyword") keyword : String
    ): List<PodcastDTO>

    @FormUrlEncoded
    @POST("webservice/addtofavpodcast")
    suspend fun getSetFavUNFav(
        @Field("podcast_id")podcast_id : String,
        @Field("user_id") user_id : String
    ): Boolean
}