package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.poses.CategoryDTO
import com.dbvertex.job.network.response.poses.PosesImagesDTO
import com.dbvertex.job.network.response.poses.SubcategoryDTO
import retrofit2.http.GET
import retrofit2.http.Path

interface PosesServices {

    @GET("poses/getsubcategories/{category_id}")
    suspend fun getSubcategory(
        @Path("category_id") category_id : String
    ): List<SubcategoryDTO>

    @GET("poses/categories")
    suspend fun getCategory(): List<CategoryDTO>

    @GET("poses/getimages/{subcategory_id}/{user_id}")
    suspend fun getPOsesImages(
        @Path("subcategory_id") subcategory_id : String,
        @Path("user_id") user_id : String
    ): List<PosesImagesDTO>

//http://thephototribe.in/api/poses/likeimage/{image_id}/{user_id}

    @GET("poses/likeimage/{image_id}/{user_id}")
    suspend fun Poseslikeunlike(
        @Path("image_id") image_id : String,
        @Path("user_id") user_id : String
    ): Boolean
}