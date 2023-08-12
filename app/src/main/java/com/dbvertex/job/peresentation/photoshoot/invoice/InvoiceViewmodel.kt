package com.dbvertex.job.peresentation.photoshoot.invoice

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.response.photoshoot.Invoice.InvoiceItemDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.google.gson.Gson
import kotlinx.coroutines.launch

class InvoiceViewmodel : ViewModel() {

    val createdDate = MutableLiveData<String>()

    val loadingstate =MutableLiveData<Boolean>(false)

    val itemlist  = mutableListOf<InvoiceItemDTO>()


     val itemlistrefresh = MutableLiveData<Boolean>(false)
    val perItemAmount = MutableLiveData<String>()
    val Quantity = MutableLiveData<String>()
    val GSTTYpe = MutableLiveData<String>()
    val GSTTYpeError = MutableLiveData<String>()
    val Rateinpercent = MutableLiveData<String>()
    val HSNCODE = MutableLiveData<String>()
    val FinalAmount = MutableLiveData<String>()
    val ItemDescription = MutableLiveData<String>()


    //Bill to
    val bill_to_name = MutableLiveData<String>()
    val bill_to_email = MutableLiveData<String>()
    val bill_to_phone = MutableLiveData<String>()
    val bill_to_gstnumber = MutableLiveData<String>()
    val bill_to_address = MutableLiveData<String>()
    val bill_to_city = MutableLiveData<String>()
    val bill_to_state = MutableLiveData<String>()
    val bill_to_pincode = MutableLiveData<String>()

    //Bill From
    val bill_from_name = MutableLiveData<String>()
    val bill_from_email = MutableLiveData<String>()
    val bill_from_phone = MutableLiveData<String>()
    val bill_from_gstnumber = MutableLiveData<String>()
    val bill_from_address = MutableLiveData<String>()
    val bill_from_city = MutableLiveData<String>()
    val bill_from_state = MutableLiveData<String>()
    val bill_from_pincode = MutableLiveData<String>()

    //Bank Detail
    val account_holder_name =MutableLiveData<String>()
    val account_number = MutableLiveData<String>()
    val bank_name = MutableLiveData<String>()
    val bank_ifsc = MutableLiveData<String>()
    val account_type = MutableLiveData<String>()


    val invoice_link = MutableLiveData<String>()

    /* POST : photoshoot_id, bill_to_name, bill_to_email,
     bill_to_phone, bill_to_address, bill_to_city, bill_to_state,
     bill_to_pincode, bill_from_name, bill_from_email, bill_from_phone,
     bill_from_address, bill_from_city, bill_from_state, items, bank_acc_name,
     bank_name, bank_acc_number, bank_ifsc, bank_acc_type, date_of_invoice*/

    fun getInvoice(photoshootID: String){
        itemlist.clear()

        viewModelScope.launch {
            val result = PhotoShootRepository.getInvoice(photoshootID)
            when(result){
                is ResultWrapper.Success ->{
                   val response = result.response
                    createdDate.value = response.date_of_invoice
                    bill_to_name.value = response.bill_to_name
                    bill_to_email.value = response.bill_to_email
                    bill_to_phone.value =response.bill_to_phone
                    bill_to_address.value = response.bill_to_address
                    bill_to_city.value = response.bill_to_city
                    bill_to_state.value =response.bill_to_state
                    bill_to_pincode.value =response.bill_to_pincode
                    bill_to_gstnumber.value = response.bill_to_gst_number

                    bill_from_name.value = response.bill_from_name
                    bill_from_email.value = response.bill_from_email
                    bill_from_phone.value = response.bill_from_phone
                    bill_from_address.value = response.bill_from_address
                    bill_from_city.value = response.bill_from_city
                    bill_from_state.value = response.bill_from_state
                    bill_from_pincode.value =response.bill_from_pincode
                    bill_from_gstnumber.value =response.bill_from_gst_number

                    itemlist.addAll(response.items)
                    itemlistrefresh.value = true
                    account_holder_name.value = response.bank_acc_name
                    bank_name.value = response.bank_name
                    account_number.value = response.bank_acc_number
                    bank_ifsc.value = response.bank_ifsc
                    account_type.value = response.bank_acc_type

                    invoice_link.value = response.invoice_link

                }
                is ResultWrapper.Failure ->{

                    createdDate.value = ""
                    bill_to_name.value =""
                    bill_to_email.value = ""
                    bill_to_phone.value =""
                    bill_to_address.value = ""
                    bill_to_city.value = ""
                    bill_to_state.value =""
                    bill_to_pincode.value =""
                    bill_to_gstnumber.value = ""

                    bill_from_name.value =""
                    bill_from_email.value = ""
                    bill_from_phone.value = ""
                    bill_from_address.value = ""
                    bill_from_city.value = ""
                    bill_from_state.value = ""
                    bill_from_pincode.value =""
                    bill_from_gstnumber.value =""

                    account_holder_name.value = ""
                    bank_name.value = ""
                    account_number.value =""
                    bank_ifsc.value =""
                    account_type.value = ""

                    invoice_link.value = ""
                }
            }
        }
    }


    fun postInvoice(photoshootID : String){
        loadingstate.value = true
        val gson = Gson()
        val items = gson.toJson(itemlist)
        viewModelScope.launch {

            val result = PhotoShootRepository.postInvoice(
               photoshoot_id = photoshootID,
                bill_to_name = bill_to_name.value ?: " ",
                bill_to_email = bill_to_email.value ?: " ",
                bill_to_phone = bill_to_phone.value ?: " ",
                bill_to_address = bill_to_address.value ?: " ",

                bill_to_city =  bill_to_city.value ?: " ",
                bill_to_state = bill_to_state.value ?: " ",
                bill_to_pincode = bill_to_pincode.value ?: " ",
                bill_to_gst_number = bill_to_gstnumber.value ?: " ",
                bill_from_name = bill_from_name.value ?: " ",
                bill_from_email = bill_from_email.value ?: " ",
                bill_from_phone = bill_from_phone.value ?: " ",
                bill_from_address = bill_from_address.value ?: " ",
                bill_from_city = bill_from_city.value ?: " ",
                bill_from_state = bill_from_state.value ?: " ",
                bill_from_gst_number = bill_from_gstnumber.value ?: " ",
                bill_from_pincode =bill_from_pincode.value ?: " ",
                items = items ,
                bank_acc_name = account_holder_name.value ?: " ",
                bank_name = bank_name.value ?: " ",
                bank_acc_number = account_number.value ?: " ",
                bank_ifsc = bank_ifsc.value ?: " ",
                bank_acc_type = account_type.value ?: " ",
                date_of_invoice = createdDate.value ?: " "
            )
            when(result){
                is ResultWrapper.Success ->{
                    loadingstate.value = false
                    invoice_link.value = result.response.toString()
                    temp_showToast("Saved")


                }
                is ResultWrapper.Failure ->{
                    loadingstate.value = false
                    temp_showToast("${result.errorMessage}")
                }
            }
        }
    }
}