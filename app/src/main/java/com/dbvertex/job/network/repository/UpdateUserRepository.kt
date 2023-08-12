package com.dbvertex.job.network.repository

import android.util.Log
import com.dbvertex.job.data.UserEntity
import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.UpdateUserServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper

object UpdateUserRepository {

    val updateUserServices = getRetrofitService(UpdateUserServices::class.java)
    val user_id = SharedPrefrenceHelper.user.userid


    suspend fun setEvent(todate : String, fromdate : String, event : String, reminder : Int) = safelyCallApi {
        updateUserServices.setEvent(user_id.toString(), todate, fromdate, event, reminder)
    }

    suspend fun getEvent(viewid : String) = safelyCallApi {
        updateUserServices.getEvent(user_id.toString(), viewid)
    }


    suspend fun getOtherUserDetail() = safelyCallApi {
        updateUserServices.getOtherProfile(user_id.toString())
    }
    suspend fun updateOtheruser(userID: String, specialisation: String ,sociallink : String, review : String) =
        safelyCallApi {

            updateUserServices.updateOtherProfile(user_id = userID, specialisation = specialisation, sociallink = sociallink, review = review)

        }

    suspend fun getUserPersonaldetail(uid:String) = safelyCallApi {
        Log.d("personaldata",uid.toString())
        updateUserServices.getPersonalDetail(uid)
    }

    suspend fun updateCompanyUser( userID : String,companyname : String, companyaddress: String ,
                                specialisation: String, experience: String,
                                socialID: String, assignment : Int, about : String) =
        safelyCallApi {
            updateUserServices.UpdateCompanyProfile(userID, comapany_name = companyname, company_address = companyaddress,
                specialisations = specialisation, experience = experience, socialid = socialID, assignmentBoolean = assignment, about = about)
        }


    suspend fun getSingleProfessionalUser(userID: String)  = safelyCallApi {

        updateUserServices.getFreelancerUserDetail(userID)
    }


    suspend fun Updatepersonaluser( firstname : String,
                              lastname : String,

                              dob : String,
                              manuallocation : String,
                              apilocation : String,


                              username : String,
                            )
            = safelyCallApi {

        val result =    updateUserServices.Updatepersonaluser(
            user_id.toString() ,firstname, lastname,  dob, manuallocation, apilocation,  username)


        val userdto = result.data
        userdto?.let {
            SharedPrefrenceHelper.user = UserEntity(
                userdto.id,
                "${userdto.first_name} ${userdto.last_name}",
                userdto.user_category,
                userdto.phone,
                userprofile = null,
                userVerfiied = false
            )
        }
        result

    }

    /* http://thephototribe.in/api/webservice/updatefreelanceruser
    POST : user_id, category, specialisation, experience, equipments, passport(Boolean), aboutme*/

    suspend fun PostFreelancerUpdateUser(
        category : String,
        specialisation : String,
        experience : String,
        equipments : String,
        passport : Int,
        aboutme : String,
    ) = safelyCallApi {
        updateUserServices.postFreelanceUpdate(user_id.toString(), category, specialisation, experience, equipments, passport, aboutme)
    }

}