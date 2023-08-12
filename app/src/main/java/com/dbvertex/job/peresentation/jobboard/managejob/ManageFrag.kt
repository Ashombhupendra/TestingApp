package com.dbvertex.job.peresentation.jobboard.managejob

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentManageBinding
import com.dbvertex.job.network.response.jobboard.jobsapplieduserlist
import com.dbvertex.job.peresentation.jobboard.JobboardViewmodel
import com.google.android.material.transition.MaterialFadeThrough
import com.google.gson.Gson

class   ManageFrag : Fragment(), onManageJobCLick {

    private lateinit var mBinding : FragmentManageBinding
    private  val mJobviewmodel   by activityViewModels<JobboardViewmodel>()

    private lateinit var adapter: ManageJobAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enterTransition = MaterialFadeThrough().apply {
            duration = 500
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentManageBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@ManageFrag
            viewmodel=  mJobviewmodel


        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.refresh.setOnRefreshListener {
            mJobviewmodel.mapplieduser()
            mJobviewmodel.mPosteduser()
            mBinding.refresh.isRefreshing=false
        }

        mJobviewmodel.mapplieduser()
        mJobviewmodel.applieduser.observe(viewLifecycleOwner, Observer {
            mJobviewmodel.managejob_apply_counter.value = "Job you applied (${it.size})"
               if (it.isNullOrEmpty()){
                   mBinding.noAppliedJobFound.visibility = View.VISIBLE
               }else{
                   mBinding.noAppliedJobFound.visibility = View.GONE
               }

             adapter = ManageJobAdapter(JobApp.instance.applicationContext, it as ArrayList<jobsapplieduserlist>,this)
             mBinding.managejobRvApplied.adapter = adapter
        })

        mJobviewmodel.mPosteduser()
        mJobviewmodel.postedjobs.observe(viewLifecycleOwner, Observer {
            mJobviewmodel.managejob_post_counter.value = "Job you posted (${it.size})"
            if (it.isNullOrEmpty()){
                mBinding.noPostedJobFound.visibility = View.VISIBLE
            }else{
                mBinding.noPostedJobFound.visibility = View.GONE
            }
             adapter = ManageJobAdapter(JobApp.instance.applicationContext, it as ArrayList<jobsapplieduserlist>, this)
            mBinding.managejobRvPosted.adapter = adapter
        })

        mJobviewmodel.updateadapterdlt.observe(viewLifecycleOwner, Observer {
            if (it){
                adapter.notifyDataSetChanged()
                mJobviewmodel.mPosteduser()
                mJobviewmodel.mapplieduser()
                mJobviewmodel.updateadapterdlt.value = false
            }

        })
    }

    override fun onMenuItemClick(applieduser: jobsapplieduserlist, mview: View) {
        val popup = PopupMenu(context, mview)
        popup.apply {
            inflate(R.menu.managejob_menu)
            setOnMenuItemClickListener {
                onMenuClick(it.itemId, applieduser)

                true
            }
            show()
        }
    }

    private fun onMenuClick(itemid: Int, applieduser: jobsapplieduserlist){

        when(itemid){
            R.id.edit_job ->{
                //Toast.makeText(requireContext(), "Edit Job", Toast.LENGTH_SHORT).show()
                val productDtoString = Gson().toJson(applieduser)
                findNavController().navigate(R.id.updateJob ,
                    bundleOf("jobdetail" to productDtoString)
                )
            }
            R.id.delete_job ->{
                deletejob(applieduser.job_id)
            }
        }

    }

    override fun onManageJobCLick(applieduser: jobsapplieduserlist) {
        mJobviewmodel.getjoberdetail(applieduser.job_id)
        findNavController().navigate(R.id.jobBoardDetail)
    }

    override fun onPause() {
        super.onPause()
        mJobviewmodel.postedjobs.removeObserver { viewLifecycleOwner }
        mJobviewmodel.applieduser.removeObserver {viewLifecycleOwner}
    }

    override fun onResume() {
        super.onResume()
        mJobviewmodel.mPosteduser()
        mJobviewmodel.mapplieduser()

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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mJobviewmodel.postedjobs.removeObserver { viewLifecycleOwner }
        mJobviewmodel.applieduser.removeObserver {viewLifecycleOwner}
    }


}