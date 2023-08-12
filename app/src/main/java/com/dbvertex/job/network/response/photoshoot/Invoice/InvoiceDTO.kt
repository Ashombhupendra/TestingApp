package com.dbvertex.job.network.response.photoshoot.Invoice

import com.google.gson.annotations.SerializedName

data class InvoiceDTO(
    @SerializedName("id")
    val id : String,
    @SerializedName("photoshoot_id")
    val photoshoot_id : String,
    @SerializedName("bill_to_name")
    val bill_to_name : String,
    @SerializedName("bill_to_email")
    val bill_to_email  : String,
    @SerializedName("bill_to_phone")
    val bill_to_phone : String,
    @SerializedName("bill_to_address")
    val bill_to_address : String,
    @SerializedName("bill_to_city")
    val bill_to_city : String,
    @SerializedName("bill_to_state")
    val bill_to_state : String,

    @SerializedName("bill_to_gst_number")
    val bill_to_gst_number :String,
    @SerializedName("bill_to_pincode")
    val bill_to_pincode : String,
    @SerializedName("bill_from_name")
    val bill_from_name : String,
    @SerializedName("bill_from_email")
    val bill_from_email : String,
    @SerializedName("bill_from_phone")
    val bill_from_phone : String,
    @SerializedName("bill_from_address")
    val bill_from_address : String,
    @SerializedName("bill_from_city")
    val bill_from_city : String,

    @SerializedName("bill_from_gst_number")
    val bill_from_gst_number : String,

    @SerializedName("bill_from_state")
    val bill_from_state : String,
    @SerializedName("bill_from_pincode")
    val bill_from_pincode : String,

    @SerializedName("items")
    val items : List<InvoiceItemDTO>,

    @SerializedName("bank_acc_name")
    val bank_acc_name : String,
    @SerializedName("bank_name")
    val bank_name : String,
    @SerializedName("bank_acc_number")
    val bank_acc_number : String,
    @SerializedName("bank_ifsc")
    val bank_ifsc : String,
    @SerializedName("bank_acc_type")
    val bank_acc_type : String,
    @SerializedName("date_of_invoice")
    val date_of_invoice : String,

    @SerializedName("invoice_link")
    val invoice_link : String


    )
/*
{
    "id": "1",
    "photoshoot_id": "20",
    "bill_to_name": "Dr. Vivek Bindra",
    "bill_to_email": "bindravivek@gmail.com",
    "bill_to_phone": "9741147815",
    "bill_to_address": "Navi Mumbai chowk",
    "bill_to_city": "mumbai",
    "bill_to_state": "maharastra",
    "bill_to_pincode": "147852",
    "bill_from_name": "Aayush Solanki",
    "bill_from_email": "aayushsolanki40@gmail.com",
    "bill_from_phone": "9399400758",
    "bill_from_address": "Chandni Chowk",
    "bill_from_city": "Ratlam",
    "bill_from_state": "Madhya Pradesh",
    "bill_from_pincode": "456211",
    "items": [
    {
        "item_description": "Item 1",
        "tax_type": "CGST",
        "tax_rate": 18,
        "item_amount": 1000,
        "item_quantity": 2,
        "total_amount": 2000,
        "final_amount": 2360
    }
    ],
    "bank_acc_name": "Aayush Solanki",
    "bank_name": "Bank of India",
    "bank_acc_number": "91121000407",
    "bank_ifsc": "BKID000505",
    "bank_acc_type": "Saving",
    "date_of_invoice": "2021-08-25"
}*/
