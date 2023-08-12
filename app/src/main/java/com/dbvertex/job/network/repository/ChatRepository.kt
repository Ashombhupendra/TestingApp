package com.dbvertex.job.network.repository

import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.ChatServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.utils.SharedPrefrenceHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody


object ChatRepository {

    val chatservices = getRetrofitService(ChatServices::class.java)
    val muser_id = SharedPrefrenceHelper.user.userid


    suspend fun getChatUserList(userid:String) = safelyCallApi {
        chatservices.getChatUser(userid)//muser_id.toString()
    }

    suspend fun getpdfurl() = safelyCallApi {
        chatservices.getPDFnavigate(user_id = muser_id.toString())
    }
    suspend fun getChatList(senderid : String,userid: String) = safelyCallApi {
        chatservices.getChatList(userid, senderid)
    }

    suspend fun sendtextmessage(
        senderid: String,
         message : String,
        userid: String
    ) = safelyCallApi {
        temp_showToast("sender:"+muser_id.toString().toString())
        chatservices.sendtextmessage(userid, senderid, message)
    }


    suspend fun sendMultimediamsg(
        senderid: String,
        images: Pair<ByteArray, String>?,
        postAudio: ByteArray?,
        video: Pair<ByteArray, String>?,
        pdf: ByteArray?,
        pdf_name :String?
    ) = safelyCallApi {

        // sender_id, receiver_id, filetoupload(file)
        if (video != null){
            val videoRequestBody = video.first.toRequestBody("video/*".toMediaType())
            val videoMultipart = videoRequestBody.let {
                MultipartBody.Part.createFormData(
                    "filetoupload",
                    video.second,
                    videoRequestBody
                )
            }
            val user_id_multipart = MultipartBody.Part.createFormData("sender_id", muser_id.toString())
            val senderid_multipart = MultipartBody.Part.createFormData("receiver_id", senderid)


            chatservices.sendMultimediamsg(user_id_multipart, senderid_multipart,videoMultipart)

        }else if(postAudio != null){
            val postAudioMultipart = postAudio?.run {
                MultipartBody.Part.createFormData(
                    "filetoupload",
                    "audio_crib.3gp",
                    postAudio.toRequestBody("application/json".toMediaType())
                )
            }
            val user_id_multipart = MultipartBody.Part.createFormData("sender_id", muser_id.toString())
            val senderid_multipart = MultipartBody.Part.createFormData("receiver_id", senderid)

            chatservices.sendMultimediamsg(user_id_multipart, senderid_multipart,postAudioMultipart)

        }else if (pdf != null){


            val pdfMultipart = pdf?.run {
                MultipartBody.Part.createFormData(
                    "filetoupload",
                    pdf_name,
                    pdf.toRequestBody("application/json".toMediaType())
                )
            }
            val user_id_multipart = MultipartBody.Part.createFormData("sender_id", muser_id.toString())
            val senderid_multipart = MultipartBody.Part.createFormData("receiver_id", senderid)
            chatservices.sendMultimediamsg(user_id_multipart, senderid_multipart,pdfMultipart)


        }else{
             val imageRequestBody = images?.first?.toRequestBody("image/*".toMediaType())
            val mimagerequestbody= MultipartBody.Part.createFormData("filetoupload", images!!.second,imageRequestBody!!)
//         val mimagerequestbody = imageRequestBody?.let {
//             MultipartBody.Part.createFormData("filetoupload", images.second,
//                 it
//             )
//         }
            val user_id_multipart = MultipartBody.Part.createFormData("sender_id", muser_id.toString())
            val senderid_multipart = MultipartBody.Part.createFormData("receiver_id", senderid)
            chatservices.sendMultimediamsg(user_id_multipart, senderid_multipart,
                mimagerequestbody!!
            )


        }

    }
}