package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.FreelancerAboutmeProRes
import com.dbvertex.job.network.response.FreelancerHeaderProRES
import com.dbvertex.job.network.response.GalleryDto
import com.dbvertex.job.network.response.ratingres.RatingDTO
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import retrofit2.http.*

interface FreelancerProfileServices {




    //POST: rater_id, ratee_id, creativity(Float),
    //http://thephototribe.in/api/webservice/ratings
    // punctuality(Float), communication(Float) , presentation(Float), workethics(Float), comment
//http://thephototribe.in/api/webservice/ratings/{user_id}
    @GET("webservice/ratings/{user_id}")
    suspend fun getRatings(
        @Path("user_id") user_id: String
    ): RatingDTO

    @POST("webservice/ratings")
    @FormUrlEncoded
    suspend fun setRating(
        @Field("rater_id")rater_id : String,
        @Field("ratee_id")ratee_id : String,
        @Field("creativity")creativity : Float,
        @Field("punctuality")punctuality : Float,
        @Field("communication")communication : Float,
        @Field("presentation")presentation : Float,
        @Field("workethics")workethics : Float,
        @Field("comment") comment : String

    )

      @POST("webservice/logout")
      @FormUrlEncoded
     suspend fun logoutuser(
         @Field("user_id") user_id: String ,
         @Field("device_id") device_id : String
     ) : Boolean


    @POST("webservice/updateprofilepic")
    @Multipart
    suspend fun uploadProfileImage(
        @Part user_id: MultipartBody.Part,
        @Part type: MultipartBody.Part,
        @Part profile_img : MultipartBody.Part
    ) : JsonObject





    //http://thephototribe.in/api/discuss/removeprofileimage
    @GET("webservice/removeprofileimage/{user_id}/{type}")
    suspend fun removeProfileImage(
        @Path("user_id") user_id : String,
        @Path("type") type : String
    ) : JsonObject


    //http://thephototribe.in/api/webservice/profilefreelanceraboutme/{user_id}
    @GET("webservice/profileaboutme/{user_id}")
    suspend fun getAboutMe(
        @Path("user_id") user_id : String
    ) : FreelancerAboutmeProRes


    @GET("webservice/profileheader/{user_id}/{sender_id}")
    suspend fun getHeader(
        @Path("user_id") user_id : String,
        @Path("sender_id") sender_id : String
    ) : FreelancerHeaderProRES


    //http://thephototribe.in/api/webservice/favouritefreelancer/%7Bfreelancer_id%7D/%7Bvisitor_id%7D
    @GET("webservice/favouritefreelancer/{user_id}/{sender_id}")
    suspend fun setFavUnFav(
        @Path("user_id") user_id : String,
        @Path("sender_id") sender_id : String
    ) : Boolean



    @GET("webservice/profilegallery/{user_id}")
    suspend fun getGallery(
        @Path("user_id") user_id: String
    ) : GalleryDto

    @POST("webservice/uploadsampleimages")
    @Multipart
    suspend fun uploadMedia(
        @Part userid: MultipartBody.Part,

        @Part images: List<MultipartBody.Part>
    )

    @GET("webservice/deleteimage/{image_id}")
    suspend fun removegalleryImages(
        @Path("image_id") image_id : String
    )
}