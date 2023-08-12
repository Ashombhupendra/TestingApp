package com.dbvertex.job.peresentation.userprofile.gallery

import android.annotation.SuppressLint
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.MainActivity
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.DialogDiscussImageViewBinding
import com.dbvertex.job.databinding.FragmentProfileGalleryBinding
import com.dbvertex.job.network.repository.FreelancerProfileRepository
import com.dbvertex.job.network.response.imagedto
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.utils.SharedPrefrenceHelper
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch

class ProfileGallery : Fragment() , onRVItemClick{
    val REQUEST_CODE = 200
    val list = ArrayList<GalleryModel>()
    val imagelist = MutableLiveData<List<GalleryModel>>()
    private lateinit var adapter: GalleryAdapter
    val images: MutableList<Pair<ByteArray, String>> = mutableListOf()
    private lateinit var mBinding : FragmentProfileGalleryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        list.clear()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentProfileGalleryBinding.inflate(inflater, container, false).apply {
        mBinding = this
        lifecycleOwner = this@ProfileGallery
    }.root


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerview = view.findViewById<RecyclerView>(R.id.galleryimages_rv)


        if (MainActivity.profileid.value != null) {
            getImages(MainActivity.profileid.value.toString())

        }

        imagelist.observe(viewLifecycleOwner, Observer {
            mBinding.noGalleryImage.isVisible = it.isNullOrEmpty()
            if (MainActivity.profileid.value.toString().equals(SharedPrefrenceHelper.user.userid.toString())){
                adapter = GalleryAdapter(it as ArrayList<GalleryModel>,requireContext(), this)
                recyclerview.adapter = adapter
                adapter.notifyDataSetChanged()

            }else{
                adapter = GalleryAdapter(it as ArrayList<GalleryModel>,requireContext(), null)
                recyclerview.adapter = adapter
                adapter.notifyDataSetChanged()

            }
        })




    }




    fun getImages(profileid: String) {

        lifecycleScope.launch {
            val result = FreelancerProfileRepository.getGallery(profileid)
            when(result){
                is ResultWrapper.Success -> {
                    val listsss = mutableListOf<GalleryModel>()
                    listsss.addAll(result.response.images.map { toGallrylist(it)  })
                    imagelist.value = listsss

                    adapter.notifyDataSetChanged()


                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    fun toGallrylist(imagedto: imagedto) = GalleryModel(
       imagedto.imageid,
        Uri.parse(imagedto.imageurl)
    )

    override fun onReclyclerItemClicklistner(galleryModel: GalleryModel, itemView: View,position: Int) {
            gallerydeleteImageDilog(galleryModel.imageid)

    }

    override fun onImageCLick(galleryModel: GalleryModel) {
        showDialog(galleryModel.imageView)
    }

    @SuppressLint("NullSafeMutableLiveData")
    fun gallerydeleteImageDilog(imageid: String) {
        val mDialogView = LayoutInflater.from(requireContext()).inflate(R.layout.alert_dialog, null)
        //AlertDialogBuilder
        val mBuilder = androidx.appcompat.app.AlertDialog.Builder(requireContext(), R.style.dialog_background)
            .setView(mDialogView)
        val mAlertDialog = mBuilder.create()
        mAlertDialog.show()
        val btn_ok = mDialogView.findViewById<TextView>(R.id.alert_dialog_ok)
        val btn = mDialogView.findViewById<TextView>(R.id.alert_dialog_cancel)

        val text = mDialogView.findViewById<TextView>(R.id.alert_dialog_title)


        btn.setOnClickListener {

            mAlertDialog.cancel()
        }
        btn_ok.setOnClickListener {

            lifecycleScope.launch {
                val result = FreelancerProfileRepository.removeGalleryImage(imageid)
                when(result){
                    is ResultWrapper.Success ->{

                        mAlertDialog.dismiss()
                        adapter.notifyDataSetChanged()
                        getImages(MainActivity.profileid.value.toString())

                    }
                    is ResultWrapper.Failure ->{
                        Log.d("responsessss", result.errorMessage)
                        mAlertDialog.dismiss()


                    }
                }
            }
        }

    }

    private fun showDialog(image_url : Uri) {
        val mBinding = DialogDiscussImageViewBinding.inflate(LayoutInflater.from(requireActivity()))
        val builder = Dialog(requireActivity())
        builder.window?.setBackgroundDrawableResource(android.R.color.transparent)
        builder.setContentView(mBinding.root)
        builder.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT ,ViewGroup.LayoutParams.MATCH_PARENT);
        mBinding.imageViewCancel.setOnClickListener {
            builder.dismiss()
        }
        Glide.with(JobApp.instance.applicationContext).load(image_url).into(mBinding.imageViewId)


        /* builder.setCancelable(false)
         builder.setCanceledOnTouchOutside(false)*/
        builder.show()
    }
}