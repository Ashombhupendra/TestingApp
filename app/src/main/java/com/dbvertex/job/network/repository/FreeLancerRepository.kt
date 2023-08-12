package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.FreelancerServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object FreeLancerRepository {

    private val freelancerServices = getRetrofitService(FreelancerServices::class.java)

    // category, specialisation, equipments, location, todate, fromdate, budget


    suspend fun getBanner()= safelyCallApi {
        freelancerServices.getBanner()
    }



    suspend fun getFreelencerFilter(
        category : String,
        specialisation : String,
        equipments : String,
        location : String,
        todate : String,
        fromdate : String,
        budget : String
    ) = safelyCallApi {
        freelancerServices.getFreelancerFilter(
            category, specialisation, equipments, location, todate, fromdate, budget
        )
    }

    suspend fun getFreelencerFromCategory(
           category : String, order_by_created : String , order_by_budget : String,
           order_by_rating : String,order_by_location : String
    ) = safelyCallApi {
           freelancerServices.getFreelencersuser(category,
               order_by_budget,
               order_by_created,
               order_by_rating,order_by_location,
               SharedPrefrenceHelper.user.userid.toString())
    }



    suspend fun getEditorChoice() = safelyCallApi {
        freelancerServices.getEditorCoice()
    }


    suspend fun getSpecialisationList() = safelyCallApi {
         freelancerServices.getfreelancerspecialisation()
    }


    suspend fun getEquipmentList() = safelyCallApi {
         freelancerServices.getEquipments()
    }

    suspend fun getManufacturedList(equipentID : String) = safelyCallApi {
         freelancerServices.getManufatures(equipment_id = equipentID)
    }

    suspend fun getModels(manufacturedID  : String) = safelyCallApi {
        freelancerServices.getMaodels(manufracturer_id = manufacturedID)
    }
}