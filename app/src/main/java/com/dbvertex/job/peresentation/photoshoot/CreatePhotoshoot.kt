package com.dbvertex.job.peresentation.photoshoot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentCreatePhotoshootBinding
import com.dbvertex.job.network.utils.isValidEmail
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.photoshoot.checklist.PhotoshootCheckList
import com.dbvertex.job.peresentation.photoshoot.contract.PhotoshootContract
import com.dbvertex.job.peresentation.photoshoot.invoice.InvoiceViewmodel
import com.dbvertex.job.peresentation.photoshoot.invoice.Photoshoot_Invoice
import com.dbvertex.job.peresentation.photoshoot.presaved.Photoshoot_Presaved_Msg
import com.dbvertex.job.peresentation.photoshoot.questionnaire.PhotoshootQuestionnaire
import com.dbvertex.job.peresentation.photoshoot.timeline.TimeLine
import com.google.android.material.tabs.TabLayoutMediator

class CreatePhotoshoot : Fragment() {
    private lateinit var mBinding : FragmentCreatePhotoshootBinding
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()
    private val mInvoiceViewModel by activityViewModels<InvoiceViewmodel>()

     var  position : Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = FragmentCreatePhotoshootBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@CreatePhotoshoot
            viewmodel= mPhotoshootViewModel

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = CreatePhotoShootAdapter(this)
        mBinding.favViewPager.adapter = adapter



        mBinding.backOfflinePodcast.setOnClickListener {
            val navOptions = NavOptions.Builder().setPopUpTo(R.id.create_Photoshoot, true).build()

            findNavController().navigate(R.id.photoshootMain, null, navOptions)

        }


        val bundle = arguments
        if (bundle != null){
            val type = bundle.getString("type_photoshoot")
            if (type.equals("update")){
                mPhotoshootViewModel.getSinglePhotoShoot()
                mPhotoshootViewModel.getPhotoShootPosesImages()

            }else{

                mPhotoshootViewModel.category_name.value = "My Photoshoot"
                mPhotoshootViewModel.photoshoot_time.value = "Select date Time*"
                mPhotoshootViewModel.photoshoot_id.value = ""
                mPhotoshootViewModel.title.value = ""
                mPhotoshootViewModel.session.value = ""
                mPhotoshootViewModel.address.value = ""
                mPhotoshootViewModel.note.value = ""
                mPhotoshootViewModel.client_name.value = ""
                mPhotoshootViewModel.my_goal.value = ""
                mPhotoshootViewModel.pre_saved_msg_list.value = emptyList()


            }
        }







        mBinding.savePhotoshoot.setOnClickListener {
            when(mBinding.favTablayout.selectedTabPosition){
                0 ->{
                   // Toast.makeText(JobApp.instance.applicationContext, "Detail Pages Saved", Toast.LENGTH_SHORT).show()
                   createPhotoshoot()
                }
                1 ->{
                    Toast.makeText(JobApp.instance.applicationContext, "POSES  Saved", Toast.LENGTH_SHORT).show()
                }
                2 ->{
                    Toast.makeText(JobApp.instance.applicationContext, "PRE-SAVED  Saved", Toast.LENGTH_SHORT).show()
                }
                3 ->{
                    Toast.makeText(JobApp.instance.applicationContext, "CHECKLIST  Saved", Toast.LENGTH_SHORT).show()
                }
                4 ->{
                    Toast.makeText(JobApp.instance.applicationContext, "TIMELINE  Saved", Toast.LENGTH_SHORT).show()
                }
                5 ->{
                    Toast.makeText(JobApp.instance.applicationContext, "Questionnaire  Saved", Toast.LENGTH_SHORT).show()
                }
                6 ->{
                    if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
                        if ( !isValidEmail(mInvoiceViewModel.bill_to_email.value.toString())){
                            showSnackBar("Please Enter valid email of Bill TO")
                        }else if (!isValidEmail(mInvoiceViewModel.bill_from_email.value.toString())){
                            showSnackBar("Please Enter valid email of Bill From")
                        }
                        else{
                            mInvoiceViewModel.postInvoice(mPhotoshootViewModel.photoshoot_id.value.toString())
                        }

                    }else{
                       showSnackBar("Firstly Create PhotoShoot")
                    }
                }


                else ->{
                    Toast.makeText(JobApp.instance.applicationContext, "Contract Pages Saved", Toast.LENGTH_SHORT).show()
                }
            }
        }


        TabLayoutMediator(mBinding.favTablayout, mBinding.favViewPager) { tab, position ->
            tab.text = when(position){
                0 ->{
                    "DETAILS"
                }
                1 ->{
                    "POSES"
                }
                2 ->{
                    "PRE-SAVED"
                }
                3 ->{
                    "CHECKLIST"
                }
                4 ->{
                    "TIMELINE"
                }
                5 ->{
                    "QUESTIONNAIRE"
                }
                6 ->{
                    "INVOICE"
                }
                else ->{
                    "CONTRACT"
                }
            }

        }.attach()
    }

    inner class CreatePhotoShootAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        override fun getItemCount() = 8

        override fun createFragment(position: Int) =   when(position){
            0 ->{
                PhootshootDatailPage()
            }
            1 ->{
                PhotoShootPoses()
            }
            2 ->{
                Photoshoot_Presaved_Msg()
            }
            3 ->{
                PhotoshootCheckList()
            }
            4 ->{
                TimeLine()
            }
            5->{
                PhotoshootQuestionnaire()
            }
            6 ->{
                Photoshoot_Invoice()
            }
            else ->{
                PhotoshootContract()
            }
        }
    }
    private  fun createPhotoshoot(){
        val titleisvalid = mPhotoshootViewModel.title.value.isNullOrEmpty()
        val sessiontype = mPhotoshootViewModel.session.value.isNullOrEmpty()
        val date = mPhotoshootViewModel.photoshoot_time.value.isNullOrEmpty()
        if (titleisvalid){
            Toast.makeText(JobApp.instance.applicationContext, "Enter Title", Toast.LENGTH_SHORT).show()
        }else if (sessiontype){
            Toast.makeText(JobApp.instance.applicationContext, "Select session type", Toast.LENGTH_SHORT).show()
        }else if (date){
            Toast.makeText(JobApp.instance.applicationContext, "Select Date and time", Toast.LENGTH_SHORT).show()
        }else{
            mPhotoshootViewModel.createPhotoShoot()
        }
    }




}
