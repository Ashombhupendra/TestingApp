package com.dbvertex.job.peresentation.photoshoot.invoice

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhotoshootInvoiceBinding
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import com.bumptech.glide.Glide
import java.util.*


class Photoshoot_Invoice : Fragment() , DatePickerDialog.OnDateSetListener
{
             private lateinit var mBinding : FragmentPhotoshootInvoiceBinding
    private val mInvoiceViewModel by activityViewModels<InvoiceViewmodel>()
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()

    var day = 0
    var month: Int = 0
    var year: Int = 0
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View
    {
        mBinding = FragmentPhotoshootInvoiceBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@Photoshoot_Invoice
            viewmodel = mInvoiceViewModel
             }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Glide.with(view).load(R.raw.progress_gif)
            .into(mBinding.categoryLoading)
        mInvoiceViewModel.loadingstate.observe(viewLifecycleOwner){
            if (it){
                mBinding.categoryLoading.visibility = View.VISIBLE
            }else{
                mBinding.categoryLoading.visibility = View.GONE

              //  mInvoiceViewModel.loadingstate.value = false
            }
        }

        mInvoiceViewModel.itemlistrefresh.observe(viewLifecycleOwner){
            if (it){
                mBinding.invoiceDateSelect.text = "${mInvoiceViewModel.createdDate.value}"
                val adapter = InvoiceItemAdapter(mInvoiceViewModel.itemlist)
                mBinding.invoiceBillItemRv.adapter = adapter
                adapter.notifyDataSetChanged()
                mInvoiceViewModel.itemlistrefresh.value = false
            }
        }
        mPhotoshootViewModel.photoshoot_id.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                mInvoiceViewModel.getInvoice(it)
            }else{
               mInvoiceViewModel.createdDate.value = "Select Date"
                mInvoiceViewModel.bill_to_name.value = ""
                mInvoiceViewModel.bill_to_email.value =""
                mInvoiceViewModel.bill_to_phone.value =""
                mInvoiceViewModel.bill_to_address.value = ""
                mInvoiceViewModel.bill_to_city.value = ""
                mInvoiceViewModel.bill_to_state.value =""
                mInvoiceViewModel.bill_to_pincode.value =""
                mInvoiceViewModel.bill_to_gstnumber.value =""

                mInvoiceViewModel.bill_from_name.value = ""
                mInvoiceViewModel.bill_from_email.value = ""
                mInvoiceViewModel.bill_from_phone.value = ""
                mInvoiceViewModel.bill_from_address.value =""
                mInvoiceViewModel.bill_from_city.value = ""
                mInvoiceViewModel.bill_from_state.value = ""
                mInvoiceViewModel.bill_from_pincode.value =""
                mInvoiceViewModel.bill_from_gstnumber.value =""

                mInvoiceViewModel.account_holder_name.value =""
                mInvoiceViewModel.bank_name.value = ""
                mInvoiceViewModel.account_number.value = ""
                mInvoiceViewModel.bank_ifsc.value =""
                mInvoiceViewModel.account_type.value = ""
                mInvoiceViewModel.itemlist.clear()
                mInvoiceViewModel.itemlistrefresh.value = true
            }
        }


        mBinding.itemPhotoshootPresavedAddNewMsg.setOnClickListener {
            findNavController().navigate(R.id.dialog_add_invoice_item)
        }

        mBinding.invoiceDateSelect.setOnClickListener {
            datepicker()
        }

        mBinding.photoshootPreviewInvoice.setOnClickListener {
            if (!mInvoiceViewModel.invoice_link.value.isNullOrEmpty()){
                val bundle = Bundle()
                bundle.putString("preview_type", "invoice")
                bundle.putString("preview_link", "${mInvoiceViewModel.invoice_link.value.toString()}" )
                findNavController().navigate(R.id.previewContract, bundle)
            }else{
                Toast.makeText(JobApp.instance.applicationContext, "Please Save Invoice", Toast.LENGTH_SHORT).show()
            }

        }
        mBinding.photoshootGenerateInvoice.setOnClickListener {
            if (!mInvoiceViewModel.invoice_link.value.isNullOrEmpty()){
                        sendEmail(mInvoiceViewModel.bill_to_email.value.toString(),"Invoice", mInvoiceViewModel.invoice_link.value.toString() )
                 }else{
                Toast.makeText(JobApp.instance.applicationContext, "Please Save Invoice", Toast.LENGTH_SHORT).show()
            }

        }



    }
    fun datepicker(){
        val calendar: Calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)

        val datePickerDialog = DatePickerDialog(requireContext(), R.style.MyDatePickerStyle, this ,year , month, day)
        datePickerDialog.show()
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val  myDay = dayOfMonth
        val myYear = year
        val  myMonth = month+1

        mBinding.invoiceDateSelect.setText("$myYear-$myMonth-$myDay")
        mInvoiceViewModel.createdDate.value = "$myYear-$myMonth-$myDay"
    }

    private fun sendEmail(recipient: String, subject: String, message: String) {
        val mIntent = Intent(Intent.ACTION_SEND)
        mIntent.data = Uri.parse("mailto:")
        mIntent.type = "text/plain"
        mIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf(recipient))
        mIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
        mIntent.putExtra(Intent.EXTRA_TEXT, message)


        try {
            //start email intent
            startActivity(Intent.createChooser(mIntent, "Choose Email Client..."))
        }
        catch (e: Exception){
            Log.d("exception", e.toString())
        }

    }
}