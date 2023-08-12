package com.dbvertex.job.peresentation.freelancer_signup

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentAddEquipmentDialogBinding
import com.dbvertex.job.network.utils.hideKeyboard
import com.dbvertex.job.network.utils.showSnackBar
import java.lang.Exception


class AddEquipmentDialog : DialogFragment() {
    private val mFreelancerViewModel by activityViewModels<FreelancerViewModel>()
    var equipmentlist = ArrayList<String>()
    var equipmentID = ArrayList<String>()
    var Manufatureslist = ArrayList<String>()
    var ManufatureslistID = ArrayList<String>()


     var equipmentname : String = ""
   var equipmentcompany : String = ""
     var equipmentModel : String = ""
    private lateinit var mBinding: FragmentAddEquipmentDialogBinding

    var Modellist = ArrayList<String>()
    val list = mutableListOf<String>()

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentAddEquipmentDialogBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@AddEquipmentDialog

            frag = this@AddEquipmentDialog



        }
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mFreelancerViewModel.getEquipment()
        Log.d("dialog", "This")

        Manufatureslist.add("Select Company")
        ManufatureslistID.add("0")
        Modellist.add("Select Model")
        mFreelancerViewModel.equipmentList.observe(requireActivity(), Observer {
            if (equipmentlist.isNullOrEmpty()){
                equipmentlist.add("Add Equipment")
                equipmentID.add("0")
                equipmentlist.addAll(it.map { it.name })
                equipmentID.addAll(it.map { it.id })

                EquipmentSpinner(equipmentlist)
            }


        })

        mFreelancerViewModel.manufactureList.observe(viewLifecycleOwner, Observer {

            Manufatureslist.addAll(it.map { it.company_name })
            ManufatureslistID.addAll(it.map { it.id })
            ManuFactureSpinner(Manufatureslist)
        })



        mFreelancerViewModel.EqModelList.observe(viewLifecycleOwner, Observer {


            Modellist.addAll(it.map { it.model_name })
            ModelSpinner(Modellist)
        })
    }

    fun  EquipmentSpinner(lists : ArrayList<String>){

        mBinding.addEquipmentSpinner.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(JobApp.instance.applicationContext, mBinding.addEquipmentSpinner)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            JobApp.instance.applicationContext,
            R.layout.item_spinner,
            lists

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
                if (position.toLong() == mBinding.addEquipmentSpinner.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinner.adapter = districtadapter
        mBinding.addEquipmentSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = lists.indexOf(conItem)
                if (indexs >=1){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
              //      Toast.makeText(requireContext(), conItem + indexs +"Listid : ${equipmentID[position]}", Toast.LENGTH_SHORT).show()
                    mFreelancerViewModel.manufactureList.postValue(emptyList())
                    mFreelancerViewModel.EqModelList.postValue(emptyList())
                    Manufatureslist.clear()
                    ManufatureslistID.clear()
                    Manufatureslist.add("Select Company")
                    ManufatureslistID.add("0")
                    mFreelancerViewModel.getManufacturers(equipmentID[position])
                    equipmentname = conItem.toString()

                }else{
                      equipmentname = ""
                    equipmentcompany = ""
                    equipmentModel = ""
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
        /**
         * Freelancer experience end
         */
    }

    fun  ManuFactureSpinner(lists : ArrayList<String>){

        mBinding.addEquipmentSpinnerCompany.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.addEquipmentSpinnerCompany)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            JobApp.instance.applicationContext,
            R.layout.item_spinner,
            lists

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
                if (position.toLong() == mBinding.addEquipmentSpinnerCompany.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinnerCompany.adapter = districtadapter
        mBinding.addEquipmentSpinnerCompany.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = lists.indexOf(conItem)
                if (indexs >0){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                    //   Toast.makeText(requireContext(), conItem + indexs +"Listid : ${equipmentID[position]}", Toast.LENGTH_SHORT).show()
                    Modellist.clear()
                    Modellist.add("Select Model")
                    mFreelancerViewModel.getModel(ManufatureslistID[position])
                  //  mFreelancerViewModel.manufature.value = conItem
                    equipmentcompany = conItem.toString()

                }else{
                     equipmentcompany = ""
                     equipmentModel = ""
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {



            }


        }
        /**
         * Freelancer experience end
         */
    }


    fun  ModelSpinner(lists : ArrayList<String>){

        mBinding.addEquipmentSpinnerModel.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                hideKeyboard(requireContext(), mBinding.addEquipmentSpinnerModel)
                return false
            }

        })

        val districtadapter = object : ArrayAdapter<String>(
            JobApp.instance.applicationContext,
            R.layout.item_spinner,
            lists

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
                if (position.toLong() == mBinding.addEquipmentSpinnerModel.selectedItemPosition.toLong() && position != 0 ){
                    tv.background = ColorDrawable(Color.parseColor("#223A54"))
                    tv.setTextColor(Color.parseColor("#ffffff"))

                }else{
                    tv.background = ColorDrawable(Color.parseColor("#ffffff"))
                    tv.setTextColor(Color.parseColor("#000000"))

                }


                return tv
            }
        }
        mBinding.addEquipmentSpinnerModel.adapter = districtadapter
        mBinding.addEquipmentSpinnerModel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {

                val conItem = districtadapter.getItem(position)
                val indexs = lists.indexOf(conItem)
                if (indexs >0){
                    //  view?.background = ColorDrawable(Color.parseColor("#FF8500"))
                    //   Toast.makeText(requireContext(), conItem + indexs , Toast.LENGTH_SHORT).show()
                    equipmentModel = conItem.toString()

                }else{
                     equipmentModel = ""
                }

            }
            override fun onNothingSelected(parent: AdapterView<*>?) {


            }


        }
        /**
         * Freelancer experience end
         */
    }


    fun submit(){

        if (equipmentname.isNullOrEmpty())
        {
            try {
                 showSnackBar("Select Equipment")
            }catch (e : Exception){
                Toast.makeText(JobApp.instance.applicationContext, "Select Equipment", Toast.LENGTH_SHORT).show()
            }
        }else if (equipmentcompany.isNullOrEmpty() ){
            try {
                showSnackBar("Select Company")
            }catch (e : Exception){
                Toast.makeText(JobApp.instance.applicationContext, "Select Company", Toast.LENGTH_SHORT).show()
            }
        }else if (equipmentModel.isNullOrEmpty()){
            try {
                showSnackBar("Select Model")
            }catch (e : Exception){
                Toast.makeText(JobApp.instance.applicationContext, "Select Model", Toast.LENGTH_SHORT).show()
            }
        }
        else {

            val specilisationlist = mutableListOf<Equipments>()
            specilisationlist.add(
                Equipments(
                equipmentname, equipmentcompany ,equipmentModel
            )
            )
            if (mFreelancerViewModel.finalequipments.value.isNullOrEmpty()){
                mFreelancerViewModel.finalequipments.value = specilisationlist
                dialog!!.dismiss()
            }else{
               if (mFreelancerViewModel.finalequipments.value!!.containsAll(specilisationlist)){
                   showSnackBar("Already add this item")
               }else{
                   mFreelancerViewModel.finalequipments.value = specilisationlist
                   dialog!!.dismiss()
               }
            }


        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }

}