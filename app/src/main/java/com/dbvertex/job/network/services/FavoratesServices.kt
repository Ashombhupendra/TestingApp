package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.BlogDTO
import com.dbvertex.job.network.response.FreelencerUserlistDTO
import com.dbvertex.job.network.response.Serieslist
import com.dbvertex.job.network.response.discuss.AllDiscussionDTO
import com.dbvertex.job.network.response.inspiration.InspirationDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface FavoratesServices {


    @GET("favourites/freelancers/{user_id}")
    suspend fun getFavFreelancer(
        @Path("user_id") user_id : String
    ):List<FreelencerUserlistDTO>

    @GET("favourites/blogs/{user_id}")
    suspend fun getFavBlogs(
        @Path("user_id") user_id : String
    ) : List<BlogDTO>


    @GET("favourites/discuss/{user_id}")
    suspend fun getFavDiscuss(
        @Path("user_id") user_id : String
    ) : List<AllDiscussionDTO>




    @GET("favourites/education/{user_id}")
    suspend fun getFavEducation(
        @Path("user_id") user_id : String
    ): List<Serieslist>


    @GET("favourites/inspirationals/{user_id}")
    suspend fun getFavInspiration(
        @Path("user_id") user_id : String
    ): List<InspirationDTO>
}