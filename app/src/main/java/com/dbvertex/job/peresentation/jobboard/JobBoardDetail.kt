package com.dbvertex.job.peresentation.jobboard

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView

import androidx.activity.OnBackPressedCallback
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentJobBoardDetailBinding
import com.dbvertex.job.network.repository.JobRepository
import com.dbvertex.job.network.utils.ResultWrapper

import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.launch

class JobBoardDetail : Fragment() {

      private lateinit var mBinding : FragmentJobBoardDetailBinding
    private  val mJobviewmodel   by activityViewModels<JobboardViewmodel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentJobBoardDetailBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@JobBoardDetail
            viewmodel = mJobviewmodel

        }
        return mBinding.root
    }
    companion object{

        val JobUserId=MutableLiveData<String>()
    }

    @SuppressLint("SuspiciousIndentation")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback: OnBackPressedCallback = object : OnBackPressedCallback(true /* enabled by default */) {
            override fun handleOnBackPressed() {
                findNavController().navigateUp()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner , callback)

        mBinding.jbdBack.setOnClickListener {
            findNavController().navigateUp()
        }

        mBinding.jdbApply.setOnClickListener {

            mJobviewmodel.applyforjob(SharedPrefrenceHelper.user.userid.toString())

        }

        mBinding.jbdProfilePicCv.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("profileid", mJobviewmodel.profile_id.value.toString())

            findNavController().navigate(R.id.FUProfile, bundle)
        }
        JobUserId.observe(viewLifecycleOwner){
            if(it!=null){
               // Toast.makeText(requireContext(),it.toString(),Toast.LENGTH_SHORT).show()
            }
        }

if(JobUserId.value.toString().equals(SharedPrefrenceHelper.user.userid.toString())){

    mBinding.jdbMessage.visibility=View.GONE
}
        mBinding.jdbMessage.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("receiver_id", mJobviewmodel.profile_id.value.toString() )
            bundle.putString("image", mJobviewmodel.profile_pic.value.toString() )
            findNavController().navigate(R.id.chatMessage, bundle)
        }

        mJobviewmodel.profile_pic.observe(viewLifecycleOwner){
            if (it.isNullOrEmpty()){

            }else{
                Picasso.get().load(it).into(mBinding.jbdProfilePic)
            }
        }
         mBinding.jbdMenu.setOnClickListener {

            val popup = PopupMenu(context, it)

                popup.apply {
                    inflate(R.menu.managejob_menu)

                    setOnMenuItemClickListener {
                        onMenuClick(it.itemId, mJobviewmodel.mjobid.value.toString())

                        true
                    }
                    show()
                }

        }

            mJobviewmodel.applyjob.observe(viewLifecycleOwner, Observer {
                Log.d("njcalled","called");
                if (it){
                    mBinding.jdbApply.text = "Decline"
                    mBinding.jdbApply.setTextColor(context?.resources?.getColorStateList(R.color.red));
                    mBinding.jdbApply.apply {
                        backgroundTintList = context.getResources().getColorStateList(R.color.red_light)
                    }
                }
                else{
                    mBinding.jdbApply.text = "Apply"
                    mBinding.jdbApply.setTextColor(context?.resources?.getColorStateList(R.color.white));
                    mBinding.jdbApply.apply {
                        backgroundTintList = context.getResources().getColorStateList(R.color.black)
                    }
                }
            })

    }
    private fun onMenuClick(itemid: Int, applieduser: String){

        when(itemid){
            R.id.edit_job ->{
                //Toast.makeText(requireContext(), "Edit Job", Toast.LENGTH_SHORT).show()
               lifecycleScope.launch {
                   val result = JobRepository.getJoberdetails(applieduser)
                   when(result){
                       is ResultWrapper.Success ->{
                           val productDtoString = Gson().toJson(result.response.user)
                           findNavController().navigate(R.id.updateJob ,
                               bundleOf("jobdetail" to productDtoString)
                           )
                       }
                       is ResultWrapper.Failure ->{

                       }
                   }
               }
            }
            R.id.delete_job ->{
                deletejob(applieduser)
            }
        }

    }
    fun  deletejob(jobId : String){
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog, null)
        //AlertDialogBuilder
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.dialog_background)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.create()
        mAlertDialog.show()
        val btn_ok = mDialogView.findViewById<TextView>(R.id.alert_dialog_ok)
        val btn = mDialogView.findViewById<TextView>(R.id.alert_dialog_cancel)

        val textmessage = mDialogView.findViewById<TextView>(R.id.alert_dialog_message)
        val title = mDialogView.findViewById<TextView>(R.id.alert_dialog_title)
        title.setText("Delete Job")
        textmessage.setText("Are you sure you want to delete Job")
        btn_ok.setText("Yes")
        btn.setText("No")
        btn.setOnClickListener {
            mAlertDialog.cancel()
        }
        btn_ok.setOnClickListener {
            mJobviewmodel.deletejob(jobId)
            mAlertDialog.dismiss()
            findNavController().navigateUp()
        }
    }

}