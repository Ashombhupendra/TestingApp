package com.dbvertex.job.network.services

import com.dbvertex.job.network.response.AuthResponse
import com.dbvertex.job.network.response.CompanyUserDTO
import com.dbvertex.job.network.response.RegistrationResponse
import org.json.JSONObject
import retrofit2.http.*

interface AuthServices {

    @GET("webservice/profileprofessional/{user_id}")
    suspend fun getCompanyUser(
        @Path("user_id") user_id: String
    ): CompanyUserDTO

    @FormUrlEncoded
    @POST("webservice/verifymobile")
    suspend fun login(
        @Field("mobile") mobile: String,
        @Field("device_id") device_id : String

    ): AuthResponse

    @FormUrlEncoded
    @POST("webservice/refreshtoken")
    suspend fun updatetoken(
        @Field("mobile") mobile: String,
        @Field("device_id") device_id : String
    ): JSONObject


    @POST("webservice/logout")
    @FormUrlEncoded
    suspend fun logoutuser(
        @Field("user_id") user_id: String ,
        @Field("device_id") device_id : String
    ) : Boolean


    @FormUrlEncoded
    @POST("webservice/user")
    suspend fun registeruser(
        @Field("uniq_id") uniq_id : String,
        @Field("device_id") device_id : String,
        @Field("device_type") device_type : String,
        @Field("first_name") firstname : String,
        @Field("last_name") last_name : String,
        @Field("mobile") mobile : String,
        @Field("dob") dob : String,
        @Field("manuallocation") country : String,
        @Field("apilocation") state : String,

        @Field("category") category : String,
        @Field("username") username : String

    ) : RegistrationResponse


    @FormUrlEncoded
    @POST("webservice/regcompanyuser")
    suspend fun registerCompanyProfile(
        @Field("user_id") user_id : String,
        @Field("comapany_name") comapany_name : String,
        @Field("company_address") company_address : String,
        @Field("specialisations") specialisations : String,
        @Field("experience") experience : String,
        @Field("socialid") socialid : String,
        @Field("assignment") assignmentBoolean : Int,
        @Field("about") about : String
    ) : JSONObject


    @FormUrlEncoded
    @POST("webservice/regotheruser")
    suspend fun registerOtherProfile(
        @Field("user_id") user_id : String,
        @Field("specialisation") specialisation : String,
        @Field("sociallink") sociallink : String,
        @Field("review") review : String
    )


    @FormUrlEncoded
    @POST("webservice/regfreelanceruser")
    suspend fun registerFreelanceuser(
        @Field("user_id") user_id : String,
        @Field("category") category : String,
        @Field("specialisation") specialisation : String,
        @Field("experience") experience : String,
        @Field("equipments") equipments : String,
        @Field("passport") passport : Int,
        @Field("aboutme") aboutme : String,
        @Field("budget") budget : String

    )
}