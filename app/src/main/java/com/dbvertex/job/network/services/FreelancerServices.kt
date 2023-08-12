package com.dbvertex.job.network.services

import com.dbvertex.job.data.SliderItem
import com.dbvertex.job.network.response.*
import retrofit2.http.*

interface FreelancerServices {



//    http://thephototribe.in/api/webservice/freelancersfilter
    // category, specialisation, equipments, location, todate, fromdate, budget

    @GET("webservice/getbanner")
    suspend fun getBanner():List<SliderItem>


    @POST("webservice/freelancersfilter")
    @FormUrlEncoded
    suspend fun getFreelancerFilter(
              @Field("category") category: String,
              @Field("specialisation") specialisation: String,
              @Field("equipments") equipments: String,
              @Field("location") location: String,
              @Field("todate") todate: String,
              @Field("fromdate") fromdate: String,
              @Field("budget") budget: String
    ) : List<FreelencerUserlistDTO>


    @POST("webservice/freelancerslist")
    @FormUrlEncoded
    suspend fun getFreelencersuser(
        @Field("category")category : String,
        @Field("order_by_budget") order_by_budget: String,
        @Field("order_by_created") order_by_created: String,
        @Field("order_by_rating") order_by_rating: String,
        @Field("order_by_location") order_by_location: String,
        @Field("user_id") user_id: String
    ) : List<FreelencerUserlistDTO>


    @GET("webservice/getfreelancerspecialisation")
    suspend fun getfreelancerspecialisation() : List<SpecialisationDTO>


    @GET("webservice/getequipments")
    suspend fun getEquipments() : List<EquipmentDto>


    @GET("webservice/getmanufracture/{equipment_id}")
    suspend fun getManufatures(
        @Path("equipment_id") equipment_id : String
    ):  List<ManufatureDto>

    @GET("webservice/getmodels/{manufracturer_id}")
    suspend fun getMaodels(
        @Path("manufracturer_id") manufracturer_id : String
    ):  List<EqModelsdto>

    @GET("webservice/getfreelancereditorchoice")
    suspend fun getEditorCoice() : EditerChoiceDto
}