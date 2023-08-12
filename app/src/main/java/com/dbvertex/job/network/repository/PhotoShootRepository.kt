package com.dbvertex.job.network.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.dbvertex.job.network.repository.photoshootposes.PosesPagingSorces
import com.dbvertex.job.network.retrofit.getRetrofitService
import com.dbvertex.job.network.services.PhotoShootServices
import com.dbvertex.job.network.utils.safelyCallApi
import com.dbvertex.job.utils.SharedPrefrenceHelper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

object PhotoShootRepository {
    private val phootshootservices = getRetrofitService(PhotoShootServices::class.java)
    val user_id = SharedPrefrenceHelper.user.userid
    private const val PRODUCTS_PAGE_SIZE = 3

    suspend fun createPhotoShoot(
        photoshoot_id: String,
        title : String, session: String, photoshoot_time: String,
                                 address: String, note: String, client_name: String, my_goal: String)=
        safelyCallApi {
            phootshootservices.postphotoshoot(photoshoot_id =  photoshoot_id, user_id.toString(), title, session, photoshoot_time, address, note, client_name, my_goal)
        }


    suspend fun getSessionType() = safelyCallApi {
        phootshootservices.getSessionType()
    }

    suspend fun getUpcomingPhotoshoot()= safelyCallApi {
        phootshootservices.getUpcomingPhotoshoot(user_id.toString())
    }

    suspend fun getCompletePhotoshoot()= safelyCallApi {
        phootshootservices.getCompletePhotoshoot(user_id.toString())
    }

    suspend fun getSinglePhotoshoot(photoshoot_id :String)= safelyCallApi {
        phootshootservices.getSinglePhotoshoot(user_id.toString(), photoshoot_id)
    }

    suspend fun getImagesAddPhotoshoot(image_id :String) = safelyCallApi {
        phootshootservices.getImageAddPhotoshoot(user_id.toString(), image_id)
    }

    suspend fun addImagetoPhotoshoot(photoshoot_id :String,pose_id :String) = safelyCallApi {
        phootshootservices.AddPhotoshootImage(photoshoot_id, pose_id)
    }

    suspend fun deletePosesImage(image_id: String)= safelyCallApi {
        phootshootservices.removephotoshootposesImage(image_id)
    }

    fun getAllPhotoShootPoses(photoshoot_id: String)=Pager (
        config = PagingConfig(
        pageSize = PRODUCTS_PAGE_SIZE,
        enablePlaceholders = false
    ),
    pagingSourceFactory = {
        PosesPagingSorces(phootshootservices,  photoshoot_id )

    }
    ).flow


    suspend fun getPhotoshootQuestionnaire(photoshoot_id: String, session: String)= safelyCallApi {
        phootshootservices.getPhotoshootQuestinnnaire(photoshoot_id, session)
    }

    suspend fun editPhotoShootQuestionnaire(photoshoot_id: String, session: String,
                                            question_id : String, question: String, type : String) =
        safelyCallApi{

       phootshootservices.EditPhotoShootQuestion(photoshoot_id = photoshoot_id, session_id = session,
           question = question, question_id = question_id, type = type)
    }

    suspend fun AddPhotoShootQuestionnaire(photoshoot_id: String, session: String,
                                            question : String) =
        safelyCallApi{

            phootshootservices.AddPhotoShootQuestion(photoshoot_id = photoshoot_id, session_id = session,
                question = question)
        }
    suspend fun DeletePhotoShootQuestionnaire(photoshoot_id: String,
                                           question_id : String, type: String) =
        safelyCallApi{

            phootshootservices.DeletePhotoShootQuestion(photoshoot_id = photoshoot_id,
                question_id = question_id,type = type)
        }
    suspend fun getPresavedMsg(photoshoot_id: String) = safelyCallApi {
        phootshootservices.getPhotoShootPresavedMsg(photoshoot_id)
    }

    suspend fun savePreSavedMsg(category_id : String, photoshoot_id: String, message : String, description :String)=
        safelyCallApi {
            phootshootservices.SavePresavedMsg(
                category_id, photoshoot_id, message, description
            )
        }

    suspend fun SavePhotoShootTimeline(title: String, time: String, photoshoot_id: String) = safelyCallApi {
        phootshootservices.SavePhotoShootTimeline(title, time, photoshoot_id)
    }

    suspend fun getPhotoShootTimeline(photoshoot_id: String) = safelyCallApi {
        phootshootservices.getPhotoshootTimeLine(photoshoot_id)
    }

    suspend fun getPhotoShootCheckList(photoshoot_id: String) = safelyCallApi {
        phootshootservices.getPhotoshootChecklist(photoshoot_id)
    }

    suspend fun checklistcheckedUnchecked(checklist_id : String)= safelyCallApi {
           phootshootservices.ChecklistCheckedUnchecked(checklist_id)
    }

    suspend fun AddEditChecklist(photoshoot_id: String,category_id: String, checklist_id: String,  title: String)=
        safelyCallApi {


            phootshootservices.AddEditPhotoShootChecklist(photoshoot_id, category_id, checklist_id, title)
        }

    suspend fun getContract(photoshoot_id: String)= safelyCallApi {
        phootshootservices.getContract(photoshoot_id)
    }

    suspend fun saveContract(photoshoot_id: String, content : String)= safelyCallApi {
        phootshootservices.saveContract(photoshoot_id, content)
    }
    suspend fun getContractsharelink(photoshoot_id: String) = safelyCallApi {
        phootshootservices.getContractSharelink(photoshoot_id)
    }

    suspend fun getInvoice(photoshoot_id: String)= safelyCallApi {
        phootshootservices.getInvoice(photoshoot_id)
    }

    suspend fun getContractList(photoshoot_id: String) = safelyCallApi {
        phootshootservices.getContractList(photoshoot_id)
    }

    /* POST : photoshoot_id, bill_to_name, bill_to_email,
     bill_to_phone, bill_to_address, bill_to_city, bill_to_state,
     bill_to_pincode, bill_from_name, bill_from_email, bill_from_phone,
     bill_from_address, bill_from_city, bill_from_state, items, bank_acc_name,
     bank_name, bank_acc_number, bank_ifsc, bank_acc_type, date_of_invoice*/

    suspend fun postInvoice(
        photoshoot_id: String,
        bill_to_name: String,
        bill_to_email: String,
        bill_to_phone : String,
        bill_to_address : String,
        bill_to_city: String,
        bill_to_state : String,
        bill_to_pincode: String,
        bill_to_gst_number :String,
        bill_from_name: String,
        bill_from_email: String,
        bill_from_phone: String,
        bill_from_address : String,
        bill_from_city : String,
        bill_from_state: String,
        bill_from_gst_number  : String,
        bill_from_pincode : String,
        items : String,
        bank_acc_name: String,
        bank_name: String,
        bank_acc_number: String,
        bank_ifsc: String,
        bank_acc_type: String,
        date_of_invoice: String

    )= safelyCallApi {

          phootshootservices.postInvoice(
            mapOf(
                "photoshoot_id" to photoshoot_id,
                "bill_to_name" to bill_to_name,
                "bill_to_email" to bill_to_email,
                "bill_to_phone" to bill_to_phone,
                "bill_to_address" to bill_to_address,
                "bill_to_gst_number" to bill_to_gst_number,
                "bill_to_city" to bill_to_city,
                "bill_to_state" to bill_to_state,
                "bill_to_pincode" to bill_to_pincode,
                "bill_from_name" to bill_from_name,
                "bill_from_email" to bill_from_email,
                "bill_from_phone" to bill_from_phone,
                "bill_from_address" to bill_from_address,
                "bill_from_city" to bill_from_city,
                "bill_from_state" to bill_from_state,
                "bill_from_gst_number" to bill_from_gst_number,
                "bill_from_pincode" to bill_from_pincode,
                "items" to items,
                "bank_acc_name" to bank_acc_name,
                "bank_name" to bank_name,
                "bank_acc_number" to bank_acc_number,
                "bank_ifsc" to bank_ifsc,
                "bank_acc_type" to bank_acc_type,
                "date_of_invoice" to date_of_invoice
            )
          )
    }

    suspend fun postposesImage(
        photoshoot_id: String,
        images: Pair<ByteArray, String>
    )= safelyCallApi {
        val imageRequestBody = images.first.toRequestBody("image/*".toMediaType())
        val image = imageRequestBody.let {
            MultipartBody.Part.createFormData("pose_image", images.second,
                it
            )
        }
        val photoshootID = MultipartBody.Part.createFormData("photoshoot_id", photoshoot_id)

         phootshootservices.postposesImage(photoshootID, image)

    }


    suspend fun getTimelinestaus(time_line_id : String)= safelyCallApi {
        phootshootservices.getTimelinestatus(time_line_id)
    }

}