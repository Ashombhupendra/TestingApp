package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.BlogBannerDto
import com.dbvertex.job.network.response.BlogDTO
import com.dbvertex.job.network.response.SignleBlogDTO
import retrofit2.http.*

interface BlogServices {

    @GET("webservice/blogsbanner/{user_id}")
    suspend fun getBlogBanner(
        @Path("user_id") user_id : String
    ) : List<BlogBannerDto>



    @GET("webservice/blogs/{user_id}")
    suspend fun getAllBlogs(
        @Path("user_id") user_id : String
    ) : List<BlogDTO>

    @POST("webservice/blogsearch")
    @FormUrlEncoded
    suspend fun searchblog(
        @Field("user_id")user_id: String,
        @Field("keyword") keyword : String
    ) : List<BlogDTO>

    @GET("webservice/blog/{blog_id}/{user_id}")
    suspend fun getSingleBlog(
        @Path("blog_id") blogID : String,
        @Path("user_id") user_id: String
    ) : SignleBlogDTO


    //http://thephototribe.in/api/webservice/addtofavblog
    @POST("webservice/addtofavblog")
    @FormUrlEncoded
    suspend fun addtofav(
        @Field("blog_id") blogID : String,
        @Field("user_id") user_id: String
    )
}