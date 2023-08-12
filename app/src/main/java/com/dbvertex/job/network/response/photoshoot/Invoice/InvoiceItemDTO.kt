package com.dbvertex.job.network.response.photoshoot.Invoice

import com.google.gson.annotations.SerializedName

data class InvoiceItemDTO(

    @SerializedName("item_description")
    val itemDescription  : String,

    @SerializedName("item_amount")
    val amount  : Int,

    @SerializedName("item_quantity")
    val quantity  : Int,

    @SerializedName("tax_type")
    val gst_type  : String,

    @SerializedName("tax_rate")
    val rate  : Int,

    @SerializedName("hsn_code")
    val hsncode  : String,

    @SerializedName("final_amount")
    val  final_amount :Int

)
