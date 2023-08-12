package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.*
import com.google.gson.JsonObject
import org.json.JSONObject
import retrofit2.http.*

interface UpdateUserServices {



    //http://thephototribe.in/api/webservice/setcalendar

    @POST("webservice/setcalendar")
    @FormUrlEncoded
    suspend fun setEvent(
        @Field("user_id") user_id: String,
        @Field("todate") todate: String,
        @Field("fromdate") fromdate: String,
        @Field("event") event: String,
        @Field("reminder") reminder: Int
        ): Boolean


    @POST("webservice/getcalendar")
    @FormUrlEncoded
    suspend fun getEvent(
        @Field("user_id") user_id : String,
        @Field("veiwer_id") veiwer_id : String
    ): List<EventDTO>



    //******other update*****//
   //http://thephototribe.in/api/webservice/updateotheruser
    @GET("webservice/profileprofessional/{user_id}")
   suspend fun getOtherProfile(
       @Path("user_id") user_id: String
   ): OtherUserDTO

    @FormUrlEncoded
    @POST("webservice/updateotheruser")
    suspend fun updateOtherProfile(
        @Field("user_id") user_id : String,
        @Field("specialisation") specialisation : String,
        @Field("sociallink") sociallink : String,
        @Field("review") review : String
    )
    //******other update end*****//



    @GET("webservice/user/{user_id}")
    suspend fun getPersonalDetail(
        @Path("user_id") user_id: String
    ):userdata


    //http://thephototribe.in/api/webservice/updatecompanyuser
    //user_id, comapany_name, company_address, specialisations, experience, socialid, assignment, about


    @FormUrlEncoded
    @POST("webservice/updatecompanyuser")
    suspend fun UpdateCompanyProfile(
        @Field("user_id") user_id : String,
        @Field("comapany_name") comapany_name : String,
        @Field("company_address") company_address : String,
        @Field("specialisations") specialisations : String,
        @Field("experience") experience : String,
        @Field("socialid") socialid : String,
        @Field("assignment") assignmentBoolean : Int,
        @Field("about") about : String
    ) : JSONObject


    @GET("webservice/profileprofessional/{user_id}")
    suspend fun getFreelancerUserDetail(
        @Path("user_id") user_id: String
    ): getSingleProfessionaluserDTO

   /* http://thephototribe.in/api/webservice/updatefreelanceruser
    POST : user_id, category, specialisation, experience, equipments, passport(Boolean), aboutme*/

   /* http://thephototribe.in/api/webservice/updatefreelanceruser
    POST : user_id, category, specialisation, experience, equipments, passport(Boolean), aboutme*/

    @POST("webservice/updatefreelanceruser")
    @FormUrlEncoded
    suspend fun postFreelanceUpdate(
        @Field("user_id") user_id : String,
        @Field("category") category : String,
        @Field("specialisation") specialisation : String,
        @Field("experience") experience : String,
        @Field("equipments") equipments : String,
        @Field("passport") passport : Int,
        @Field("aboutme") aboutme : String
    ): JsonObject

    @FormUrlEncoded
    @POST("webservice/user")
    suspend fun Updatepersonaluser(
        @Field("user_id") user_id : String,
        @Field("first_name") firstname : String,
        @Field("last_name") last_name : String,

        @Field("dob") dob : String,
        @Field("manuallocation") country : String,
        @Field("apilocation") state : String,
        @Field("username") username : String

    ) : RegistrationResponse

}