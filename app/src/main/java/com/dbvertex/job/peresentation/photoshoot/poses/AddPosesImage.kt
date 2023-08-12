package com.dbvertex.job.peresentation.photoshoot.poses

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.MainActivity
import com.dbvertex.job.R
import com.dbvertex.job.databinding.DialogAddPosesImageBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.response.photoshoot.ImagePhotoshootlistDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.temp_showToast
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import kotlinx.coroutines.launch


class AddPosesImage : DialogFragment(), onClickADD {
         private lateinit var mBinding : DialogAddPosesImageBinding
         val photoshootlsit  = MutableLiveData<List<ImagePhotoshootlistDTO>>()
    private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()
          var imageID : String = ""
         val refresh = MutableLiveData<Boolean>(false)
    private lateinit var adapter: AddImagePosesAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
        dialog?.setCanceledOnTouchOutside(false)
        dialog?.setCancelable(false)
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):  View {
        mBinding = DialogAddPosesImageBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@AddPosesImage
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mBinding.addPosesBack.setOnClickListener {
            dismiss()
        }
        val bundle = arguments
        if (bundle != null){
            val image_id = bundle.getString("image_id")
            Log.d("image_id", image_id.toString())
            imageID = image_id.toString()
            getPhotoshoots(image_id.toString())
        }
         photoshootlsit.observe(viewLifecycleOwner){
              adapter = AddImagePosesAdapter(it, this)

             mBinding.addPosesRv.adapter =adapter
             mPhotoshootViewModel.posesadapterrefresh.value = true
             adapter.notifyDataSetChanged()
         }


    }

    override fun onAdd(photoshootlistDTO: ImagePhotoshootlistDTO) {
        Log.d("image_id", imageID.toString())

        lifecycleScope.launch {
            val result = PhotoShootRepository.addImagetoPhotoshoot(photoshootlistDTO.id, imageID)
            when(result ){
                is ResultWrapper.Success ->{
                     photoshootlistDTO.isAdded = result.response
                   if (MainActivity.posesaddingprocessenable.value == true){
                       val navOptions = NavOptions.Builder().setPopUpTo(R.id.create_Photoshoot, true).build()
                       val bundle = Bundle()
                       bundle.putString("type_photoshoot", "update")

                       findNavController().navigate(R.id.create_Photoshoot, bundle,navOptions)
                   }

                      Log.d("poseslist", "success")

                     adapter.notifyDataSetChanged()
                }
                is ResultWrapper.Failure ->{

                }
            }
        }
    }

    override fun onDone(photoshootlistDTO: ImagePhotoshootlistDTO) {
        val bundle = Bundle()
        bundle.putString("type_photoshoot", "create")
        findNavController().navigate(R.id.create_Photoshoot, bundle)

    }

    fun getPhotoshoots(image_id :String){
        lifecycleScope.launch {
            val  result = PhotoShootRepository.getImagesAddPhotoshoot(image_id)
            when(result){
                is ResultWrapper.Success ->{
                    val list = mutableListOf<ImagePhotoshootlistDTO>()
                    list.addAll(result.response.map { it })
                    photoshootlsit.value = list
                }
                is  ResultWrapper.Failure ->{
                    temp_showToast("${result.errorMessage}")
                }

            }
        }

    }


}