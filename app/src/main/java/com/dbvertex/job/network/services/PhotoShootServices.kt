package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.photoshoot.*
import com.dbvertex.job.network.response.photoshoot.Invoice.InvoiceDTO
import com.dbvertex.job.network.response.photoshoot.Questionnaire.QuestionnaireDTO
import okhttp3.MultipartBody
import retrofit2.http.*

interface PhotoShootServices {


//  1  http://thephototribe.in/api/photoshoot/photoshoot
//    POST : title, session, photoshoot_time, address, note, client_name, my_goal
    @POST("photoshoot/photoshoot")
    @FormUrlEncoded
    suspend fun postphotoshoot(
            @Field("photoshoot_id")photoshoot_id : String,
            @Field("user_id")user_id : String,
            @Field("title")title : String,
            @Field("session_id")session : String,
            @Field("photoshoot_time")photoshoot_time : String,
            @Field("address")address : String,
            @Field("note")note : String,
            @Field("client_name")client_name : String,
            @Field("my_goal")my_goal : String
    ) :Int
    // 2 http://thephototribe.in/api/photoshoot/sessiontype
    @GET("photoshoot/sessiontype")
    suspend fun getSessionType() : List<SessionTypeDTO>
    /* Upcoming Photoshoots
    http://thephototribe.in/api/photoshoot/upcoming/{user_id}*/
    @GET("photoshoot/upcoming/{user_id}")
    suspend fun getUpcomingPhotoshoot(
        @Path("user_id") user_id : String
    ) : List<Upcoming_complete_Photoshoot_DTO>

    /* 3 Completed Photoshoots
    http://thephototribe.in/api/photoshoot/photoshoot/{user_id}*/
    @GET("photoshoot/completed/{user_id}")
    suspend fun getCompletePhotoshoot(
        @Path("user_id") user_id : String
    ) : List<Upcoming_complete_Photoshoot_DTO>

    // 4 http://thephototribe.in/api/photosohot/photoshoot/{User_id}/{photoshoot_id}
    @GET("photoshoot/photoshoot/{user_id}/{photoshoot_id}")
    suspend fun getSinglePhotoshoot(
        @Path("user_id") user_id : String,
        @Path("photoshoot_id") photoshoot_id : String

    ) : PhotoshootDTO


    // 5 http://thephototribe.in/api/photoshoot/photoshoots/{photoshoot_id}/{image_id}
    @GET("photoshoot/photoshoots/{photoshoot_id}/{image_id}")
    suspend fun getImageAddPhotoshoot(
        @Path("photoshoot_id") photoshoot_id : String,
        @Path("image_id") image_id : String

    ) : List<ImagePhotoshootlistDTO>



    // 6 http://thephototribe.in/api/photoshoot/addpose/{photoshoot_id}/{pose_id}
    @GET("photoshoot/addpose/{photoshoot_id}/{pose_id}")
    suspend fun AddPhotoshootImage(
        @Path("photoshoot_id") photoshoot_id : String,
        @Path("pose_id") pose_id : String
    ) : Boolean


    @GET("photoshoot/removeposes/{image_id}")
    suspend fun removephotoshootposesImage(
        @Path("image_id") image_id: String
    ):Boolean
    // 7 http://thephototribe.in/api/photoshoot/getposes/{photoshoot_id}
    //http://thephototribe.in/api/photoshoot/photoshoots/{photoshoot_id}/{image_id}
    @GET("photoshoot/getposes/{photoshoot_id}/{offset}")
    suspend fun getPhotoshootPosesImages(
        @Path("photoshoot_id") photoshoot_id : String,
        @Path("offset") offset : Int,

        ) : List<getPhotoshootPosesImagesDTO>


    // 8 http://thephototribe.in/api/photoshoot/questionaries/{photoshoot_id}/{session_type_id}.
    @GET("photoshoot/questionaries/{photoshoot_id}/{session_type_id}")
    suspend fun getPhotoshootQuestinnnaire(
        @Path("photoshoot_id") photoshoot_id : String,
        @Path("session_type_id") session_type_id : String,

        ) : List<QuestionnaireDTO>


    // 9 : session_id, question_id, photoshoot_id, question, type
    @POST("photoshoot/editquestionaries")
    @FormUrlEncoded
    suspend fun EditPhotoShootQuestion(
        @Field("photoshoot_id") photoshoot_id : String,
        @Field("session_id") session_id : String,
        @Field("question_id") question_id : String,
        @Field("question") question : String,
        @Field("type") type : String,
    ): Boolean

    // 10  http://thephototribe.in/api/photoshoot/addquestionaries    session_id, photoshoot_id, question
    @POST("photoshoot/addquestionaries")
    @FormUrlEncoded
    suspend fun AddPhotoShootQuestion(
        @Field("photoshoot_id") photoshoot_id : String,
        @Field("session_id") session_id : String,
        @Field("question") question : String

    ): Boolean

    // 11 http://thephototribe.in/api/photoshoot/removequestionaries   question_id, photoshoot_id, type
    @POST("photoshoot/removequestionaries")
    @FormUrlEncoded
    suspend fun DeletePhotoShootQuestion(
        @Field("photoshoot_id") photoshoot_id : String,
        @Field("question_id") question_id : String,
        @Field("type") type : String

    ): Boolean


    // 12 h ttp://thephototribe.in/api/photoshoot/presavedmessages/{photoshoot_id}
    @GET("photoshoot/presavedmessages/{photoshoot_id}")
    suspend fun getPhotoShootPresavedMsg(
        @Path("photoshoot_id") photoshoot_id : String,
        ) : List<PresavedDTO>

    //13. Add Pre-saved Messages
    //http://thephototribe.in/api/photoshoot/presavedmsgadd
    //POST : category_id, photoshoot_id, message

    @POST("photoshoot/presavedmsgadd")
    @FormUrlEncoded
    suspend fun SavePresavedMsg(
        @Field("category_id")category_id : String,
        @Field("photoshoot_id")photoshoot_id : String,
        @Field("message")message : String,
        @Field("description")description : String
    ): Boolean




   /* 14. Post timeline
    http://thephototribe.in/api/photoshoot/timeline
    POST : title, time, photoshoot_id*/
    @POST("photoshoot/timeline")
    @FormUrlEncoded
    suspend fun SavePhotoShootTimeline(
       @Field("title")title : String,
       @Field("time")time : String,
       @Field("photoshoot_id")photoshoot_id : String,
    )

    //15
    @GET("photoshoot/timeline/{photoshoot_id}")
    suspend fun getPhotoshootTimeLine(
        @Path("photoshoot_id")photoshoot_id: String
    ): List<PhotoshootTimelineDTO>

    //16
    @GET("photoshoot/checklist/{photoshoot_id}")
    suspend fun getPhotoshootChecklist(
        @Path("photoshoot_id")photoshoot_id: String
    ): List<PhotoShootCheckListDTO>


    // 17 http://thephototribe.in/api/photoshoot/checkliststatus/{checklist_id}

    @GET("photoshoot/checkliststatus/{checklist_id}")
    suspend fun ChecklistCheckedUnchecked(
        @Path("checklist_id")checklist_id: String
    ): Boolean




    /*18 . Add Checklist
    http://thephototribe.in/api/photoshoot/checklist
    POST : title, photoshoot_id, category_id

    5.  Update Checklist
    http://thephototribe.in/api/photoshoot/checklist
    POST : title, photoshoot_id, category_id, checklist_id
*/
    @POST("photoshoot/checklist")
    @FormUrlEncoded
    suspend fun AddEditPhotoShootChecklist(
        @Field("photoshoot_id") photoshoot_id : String,
        @Field("category_id") category_id : String,
        @Field("checklist_id") checklist_id : String,
        @Field("title") title : String,
        ): Boolean

    //19
    @GET("photoshoot/contract/{photoshoot_id}")
     suspend fun getContract(
         @Path("photoshoot_id")photoshoot_id: String): String

    //19.1
    @GET("photoshoot/contracts/{photoshoot_id}")
    suspend fun getContractList(
        @Path("photoshoot_id")photoshoot_id :String
    ): List<ContractDTO>

     //20. Save Contract
    //http://thephototribe.in/api/photoshoot/contract
    //Post :: photoshoot_id, content
     @POST("photoshoot/contract")
     @FormUrlEncoded
     suspend fun saveContract(
         @Field("contract_id")photoshoot_id: String,
         @Field("content")content :String
     )


    //21
    @GET("photoshoot/contractlink/{photoshoot_id}")
    suspend fun getContractSharelink(
        @Path("photoshoot_id")photoshoot_id: String): String

    //22
    @GET("photoshoot/invoice/{photoshoot_id}")
    suspend fun getInvoice(
        @Path("photoshoot_id")photoshoot_id: String
    ):InvoiceDTO

    //23
    @POST("photoshoot/invoice")
    @FormUrlEncoded
    suspend fun postInvoice(
        @FieldMap fieldMap: Map<String, String?>
    ):String


   /* http://thephototribe.in/api/photoshoot/addposesextra
    POST: photoshoot_id, pose_image*/
   @POST("photoshoot/addposesextra")
   @Multipart
   suspend fun postposesImage(
       @Part photoshoot_id: MultipartBody.Part,
       @Part pose_image: MultipartBody.Part
   )

   //chnage timeline status
    //http://thephototribe.in/api/photoshoot/timelinestatus{timeline_id}

   @GET("photoshoot/timelinestatus/{timeline_id}")
   suspend fun getTimelinestatus(
       @Path("timeline_id")timeline_id : String
   ): Boolean
}