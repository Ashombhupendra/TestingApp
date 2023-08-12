package com.dbvertex.job.peresentation.photoshoot.invoice

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.dbvertex.job.R
import com.dbvertex.job.databinding.DialogAddInvoiceItemBinding
import com.dbvertex.job.network.response.photoshoot.Invoice.InvoiceItemDTO
import com.dbvertex.job.network.utils.hideKeyboard
import java.lang.Exception


class Dialog_add_invoice_item : DialogFragment() {

              private lateinit var mBinding : DialogAddInvoiceItemBinding
    private val mInvoiceViewModel by activityViewModels<InvoiceViewmodel>()

    val  GSTtype = listOf(
        "Select GST Type",
        "CGST",
        "SGST",
        "IGST"
    )
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart()
    {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View
    {
        mBinding = DialogAddInvoiceItemBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@Dialog_add_invoice_item
            viewmodel = mInvoiceViewModel
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
                GSTtypedropdown()
        mBinding.invoiceItemSubmit.setOnClickListener {
            submitItem()
        }
        mBinding.invoiceItemClose.setOnClickListener {
            dialog?.dismiss()
            mInvoiceViewModel.ItemDescription.value =""
            mInvoiceViewModel.perItemAmount.value =""
            mInvoiceViewModel.GSTTYpe.value = ""
            mInvoiceViewModel.Quantity.value = ""
            mInvoiceViewModel.Rateinpercent.value =""
            mInvoiceViewModel.HSNCODE.value =""
        }

        mBinding.invoiceItemRate.doOnTextChanged { text, start, before, count ->
            if (mInvoiceViewModel.perItemAmount.value.isNullOrEmpty()){
                mBinding.invoiceItemAmount.setError("Enter Per Item Amount")
            }else if (mInvoiceViewModel.Quantity.value.isNullOrEmpty()){
                mBinding.invoiceItemQuantity.setError("Enter Items Quantity")
            }else{
                if (!text.isNullOrEmpty()){
                    if (text.toString().toInt() < 100){
                       try {
                           val peritemamount = mInvoiceViewModel.perItemAmount.value!!.toInt()
                           val quantity = mInvoiceViewModel.Quantity.value!!.toInt()
                           val t_amount  = peritemamount * quantity
                           val grossamount = t_amount * text.toString().toInt() / 100
                           val finalamount = t_amount + grossamount
                           mInvoiceViewModel.FinalAmount.value = "$finalamount"
                       }
                       catch (e : Exception){
                            Log.d("hello", e.toString())
                       }
                    }else{
                        mBinding.invoiceItemRate.setError("Rate should be less than 100 %")
                    }
                }

            }
        }

        mBinding.invoiceItemAmount.doOnTextChanged { text, start, before, count ->
            if (mInvoiceViewModel.Rateinpercent.value.isNullOrEmpty()){
                mInvoiceViewModel.FinalAmount.value = "0"
            }else if (mInvoiceViewModel.Quantity.value.isNullOrEmpty()){
                mInvoiceViewModel.FinalAmount.value = "0"
            }else{
                if (!text.isNullOrEmpty()){
                    try {
                        val rate = mInvoiceViewModel.Rateinpercent.value!!.toInt()
                        val quantity = mInvoiceViewModel.Quantity.value!!.toInt()
                        val t_amount  = text.toString().toInt() * quantity
                        val grossamount = t_amount * rate / 100
                        val finalamount = t_amount + grossamount
                        mInvoiceViewModel.FinalAmount.value = "$finalamount"
                    }catch (e : Exception){
                        Log.d("andoid", e.toString())
                    }
                }

            }
        }

        mBinding.invoiceItemQuantity.doOnTextChanged { text, start, before, count ->
            if (mInvoiceViewModel.perItemAmount.value.isNullOrEmpty()){
                mBinding.invoiceItemAmount.setError("Enter Per Item Amount")
            }else if (mInvoiceViewModel.Rateinpercent.value.isNullOrEmpty()){
                  mInvoiceViewModel.FinalAmount.value = "0"
            }else{
                if (!text.isNullOrEmpty()){
                    try {
                        val peritemamount = mInvoiceViewModel.perItemAmount.value!!.toInt()
                        val quantity = mInvoiceViewModel.Rateinpercent.value!!.toInt()
                        val t_amount  = peritemamount * text.toString().toInt()
                        val grossamount = t_amount * quantity / 100
                        val finalamount = t_amount + grossamount
                        mInvoiceViewModel.FinalAmount.value = "$finalamount"
                    }catch (e : Exception){
                        Log.d("andaus", e.toString())
                    }
                }

            }
        }

    }
    private fun submitItem(){

        val itemdescription = mInvoiceViewModel.ItemDescription.value.isNullOrEmpty()
        val peritemAmount = mInvoiceViewModel.perItemAmount.value.isNullOrEmpty()
        val gsttype = mInvoiceViewModel.GSTTYpe.value.isNullOrEmpty()
        val quantity = mInvoiceViewModel.Quantity.value.isNullOrEmpty()
        val rate = mInvoiceViewModel.Rateinpercent.value.isNullOrEmpty()
        val HSNCode = mInvoiceViewModel.HSNCODE.value.isNullOrEmpty()
        if (itemdescription){
            mBinding.invoiceItemDescription.setError("Enter Description")
        }else if (gsttype){
            mBinding.invoiceItemGstError.visibility = View.VISIBLE
        }else if (rate){
            mBinding.invoiceItemGstError.visibility = View.GONE
            mBinding.invoiceItemRate.setError("Enter tax rate")
        }else if (HSNCode){
            mBinding.invoiceItemHsnCode.setError("Enter HSN Code")
        }else if(peritemAmount){
             mBinding.invoiceItemAmount.setError("Enter Amount")
        }else if (quantity){
            mBinding.invoiceItemQuantity.setError("Enter Quantity")
        }
        else{
            mInvoiceViewModel.itemlist.add(
                InvoiceItemDTO(
                "${mInvoiceViewModel.ItemDescription.value}",
                    mInvoiceViewModel.perItemAmount.value!!.toInt(),
                    mInvoiceViewModel.Quantity.value!!.toInt(),
                "${mInvoiceViewModel.GSTTYpe.value}",
                    mInvoiceViewModel.Rateinpercent.value!!.toInt(),
                "${mInvoiceViewModel.HSNCODE.value}",
                    mInvoiceViewModel.FinalAmount.value!!.toInt()
            )
            )
            mInvoiceViewModel.itemlistrefresh.value = true
            mInvoiceViewModel.ItemDescription.value =""
            mInvoiceViewModel.perItemAmount.value =""
            mInvoiceViewModel.GSTTYpe.value = ""
            mInvoiceViewModel.Quantity.value = ""
            mInvoiceViewModel.Rateinpercent.value =""
            mInvoiceViewModel.HSNCODE.value =""
            dialog!!.dismiss()
        }
    }
    fun GSTtypedropdown(){

        /**
         * Freelancer experience start
         */
        mBinding.invoiceItemGstSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.invoiceItemGstSpinner)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            requireContext(),
            R.layout.item_spinner,
            GSTtype

        ){


            override fun getDropDownView(
                position: Int,
                convertView: View?,
                parent: ViewGroup
            ): View {
                val tv : TextView = super.getDropDownView(position, convertView, parent) as TextView
                // set item text size
                //tv.setTextSize(TypedValue.COMPLEX_UNIT_SP,12F)
                // set selected item style
                if (position.toLong() == mBinding.invoiceItemGstSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.invoiceItemGstSpinner.adapter = districtadapter
        mBinding.invoiceItemGstSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = GSTtype.indexOf(conItem)
                if (indexs >=1){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                  //  Toast.makeText(requireContext(), conItem + indexs, Toast.LENGTH_SHORT).show()
                    mBinding.invoiceItemGstError.visibility = View.GONE
                     mInvoiceViewModel.GSTTYpe.value = conItem

                }else{

                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
        /**
         * Freelancer experience end
         */
    }

}