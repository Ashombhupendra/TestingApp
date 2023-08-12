package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.FreelancerProfileServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object FreelancerProfileRepository {
    private val freelancerProfileServices = getRetrofitService(FreelancerProfileServices::class.java)

    val userid  = SharedPrefrenceHelper.user.userid


    suspend fun getRating(user_id : String) = safelyCallApi {
        freelancerProfileServices.getRatings(user_id)
    }


    suspend fun setRating(ratee_id : String , creativity_rating: Float ,punctuality_rating: Float ,
                          communication_rating: Float ,presentation_rating: Float ,work_ethic_rating: Float ,
                         comment: String ) = safelyCallApi {
                             freelancerProfileServices.setRating(userid.toString(), ratee_id, creativity_rating,punctuality_rating ,communication_rating
                             ,presentation_rating,work_ethic_rating,comment)
    }

    suspend fun uploadprofile_img(images: Pair<ByteArray, String>?, type : String) = safelyCallApi {
        val imageRequestBody = images?.first?.toRequestBody("image/*".toMediaType())
        val mimagerequestbody = imageRequestBody?.let {
            MultipartBody.Part.createFormData("profile_pic", images.second,
                it
            )
        }
        val user_id_multipart = MultipartBody.Part.createFormData("user_id", userid.toString())
        val pro_type = MultipartBody.Part.createFormData("type", type.toString())


        freelancerProfileServices.uploadProfileImage(user_id_multipart, pro_type, mimagerequestbody!!)


    }
    suspend fun user_logout(device_id : String) = safelyCallApi {
        freelancerProfileServices.logoutuser(userid.toString(), device_id)
    }

    suspend fun removeProfileImage(type  : String) = safelyCallApi {
        freelancerProfileServices.removeProfileImage(userid.toString(), type)
    }

   suspend fun removeGalleryImage( image_id : String) = safelyCallApi {
            freelancerProfileServices.removegalleryImages(image_id)
   }


    suspend fun getAboutMe(profileid: String) = safelyCallApi {
            freelancerProfileServices.getAboutMe(profileid)
    }

    suspend fun getHeader(profileid: String, sender_id: String) = safelyCallApi {
        freelancerProfileServices.getHeader(profileid.toString(), sender_id)
    }

    suspend fun setFavUnFav(profileid: String, sender_id: String) = safelyCallApi {
        freelancerProfileServices.setFavUnFav(profileid.toString(), sender_id)
    }


    suspend fun getGallery(profileid: String) = safelyCallApi {
         freelancerProfileServices.getGallery(profileid)
    }

    suspend fun uploadImages( images: List<Pair<ByteArray, String>>)= safelyCallApi {

        val imageMultipartList = images.map {
            val imageRequestBody = it.first.toRequestBody("image/*".toMediaType())
            MultipartBody.Part.createFormData("sample_img[]", it.second, imageRequestBody)

        }
        val productIdMultipartBody = MultipartBody.Part.createFormData(
            "user_id",
            userid.toString()
        )

        freelancerProfileServices.uploadMedia(productIdMultipartBody, imageMultipartList)
    }
}