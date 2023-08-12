package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.discuss.AddQuestionResponse
import com.dbvertex.job.network.response.discuss.AllDiscussionDTO
import com.dbvertex.job.network.response.discuss.DiscussDetailDTO
import okhttp3.MultipartBody
import org.json.JSONObject
import retrofit2.http.*

interface DiscusServices {


    //http://thephototribe.in/api/discuss/answer
    //POST: question_id, sender_id, answer

    @POST("discuss/answer")
    @Multipart
    suspend fun  sendDiscussAnswer(
        @Part questionid :MultipartBody.Part,
        @Part sender_id : MultipartBody.Part,
        @Part answer : MultipartBody.Part,
        @Part images: List<MultipartBody.Part>
    ) : JSONObject


    //http://thephototribe.in/api/discuss/singlediscuss/{user_id}/{question_id}

    @GET("discuss/singlediscuss/{user_id}/{question_id}")
    suspend fun getSingleQuestion(
        @Path("user_id") user_id: String,
        @Path("question_id") questionid : String
    ) : DiscussDetailDTO


    //http://thephototribe.in/api/discuss/discuss
    @POST("discuss/discuss")
    @Multipart
    suspend fun postDiscuss(
        @Part question :MultipartBody.Part,
        @Part description : MultipartBody.Part,
        @Part  sender_id : MultipartBody.Part,
        @Part images: List<MultipartBody.Part>

        ): AddQuestionResponse


    //http://thephototribe.in/api/discuss/discuss/{user_id}

    @POST("discuss/discusses")
    @FormUrlEncoded
    suspend fun getQuestions(
               @Field("user_id") user_id : String,
               @Field("type") type : String
    ) : List<AllDiscussionDTO>



    @POST("discuss/search")
    @FormUrlEncoded
    suspend fun  getSearchQuestion(
        @Field("user_id") user_id: String,
        @Field("keyword") keyword: String

    ): List<AllDiscussionDTO>

/*    5.  Favourite and UnFavorite Question
    http://thephototribe.in/api/discuss/setfavourite
    POST : user_id, question_id*/
    @POST("discuss/setfavourite")
    @FormUrlEncoded
    suspend fun setDisfavUnfav(
    @Field("user_id") user_id: String,
    @Field("question_id") questionid : String
    ): Boolean
}