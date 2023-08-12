package com.dbvertex.job.network.repository

import com.dbvertex.job.data.UserEntity
import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.AuthServices
import com.dbvertex.job.network.utils.UniqueDeviceId
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object AuthRepository {

    private val authService = getRetrofitService(AuthServices::class.java)


    suspend fun updatetoke(phone : String , device_id : String)= safelyCallApi {
        authService.updatetoken(phone, device_id)
    }

    suspend fun getUserCompany(user_id : String) = safelyCallApi {
          authService.getCompanyUser(user_id)
    }


    suspend fun login(phone : String , device_id : String) = safelyCallApi {
       val result =   authService.login(phone, device_id)

        var res  = result.data
    //Toast.makeText(JobApp.instance.applicationContext,res?.user_id.toString(),Toast.LENGTH_SHORT).show()

            res?.let {
                SharedPrefrenceHelper.user = UserEntity(
                    res.user_id,
                    "${res.first_name} ${res.last_name}",
                    res.user_category,
                    res.phone,
                    res.profile_pic,
                    if (res.verified == "1") true else false
                )

        }

        result
    }

    suspend fun registeruser( firstname : String,
                              lastname : String,
                              mobile : String,
                              dob : String,
                              manuallocation : String,
                              apilocation : String,

                              category : String,
                              username : String,
    token: String)
    = safelyCallApi {

    val result =    authService.registeruser(
            UniqueDeviceId.getUniqueId(),token,"android" ,firstname, lastname, mobile, dob, manuallocation, apilocation, category, username)


        val userdto = result.data
        userdto?.let {
            SharedPrefrenceHelper.user = UserEntity(
                userdto.id,
                "${userdto.first_name} ${userdto.last_name}",
                userdto.user_category,
                userdto.phone,
                userprofile = userdto.profile_pic,
                false
            )
        }
        result

    }


    suspend fun regCompanyUser( userID : String,companyname : String, companyaddress: String ,
                                specialisation: String, experience: String,
                                socialID: String, assignment : Int, about : String) =
        safelyCallApi {
            authService.registerCompanyProfile(userID, comapany_name = companyname, company_address = companyaddress,
            specialisations = specialisation, experience = experience, socialid = socialID, assignmentBoolean = assignment, about = about)
        }


    suspend fun regOtheruser(userID: String, specialisation: String ,sociallink : String, review : String) =
        safelyCallApi {
            authService.registerOtherProfile(user_id = userID, specialisation = specialisation, sociallink = sociallink, review = review)
        }


    suspend fun regFreelanceruser(
        userID: String,
        category : String,
        specialisation: String,
        experience: String,
        equipment : String,
        passport : Int ,
        aboutme : String,
        budget : String
    ) = safelyCallApi {
        authService.registerFreelanceuser(user_id = userID, category = category, specialisation = specialisation,
        experience = experience, equipments = equipment,passport = passport, aboutme = aboutme, budget = budget)
    }
}