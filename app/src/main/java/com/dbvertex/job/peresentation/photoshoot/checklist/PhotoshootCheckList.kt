package com.dbvertex.job.peresentation.photoshoot.checklist

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.dbvertex.job.JobApp
import com.dbvertex.job.R
import com.dbvertex.job.databinding.FragmentPhotoshootCheckListBinding
import com.dbvertex.job.network.repository.PhotoShootRepository
import com.dbvertex.job.network.response.photoshoot.PhotoShootCheckListDTO
import com.dbvertex.job.network.utils.ResultWrapper
import com.dbvertex.job.network.utils.showSnackBar
import com.dbvertex.job.peresentation.photoshoot.PhotoShootViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import kotlinx.coroutines.launch


class PhotoshootCheckList : Fragment(), onChecklistClick {

        private  lateinit var mBinding : FragmentPhotoshootCheckListBinding
        private val mPhotoshootViewModel by activityViewModels<PhotoShootViewModel>()
    lateinit var bottomSheetDialog: BottomSheetDialog
    var  mList = MutableLiveData<List<PhotoShootCheckListDTO>>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentPhotoshootCheckListBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@PhotoshootCheckList
        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

          getChecklist()


         mList.observe(viewLifecycleOwner){
             val adapter = ChecklistAdapter(it, this)
             mBinding.photoshootCheckListRv.adapter = adapter
             adapter.notifyDataSetChanged()
         }
       /*  val swipeGesture = object  :SwipeGesture(JobApp.instance.applicationContext){
             override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                 when(direction){
                     ItemTouchHelper.LEFT ->{
                         Toast.makeText(JobApp.instance.applicationContext, "Swipe Left", Toast.LENGTH_SHORT).show()
                     }
                     ItemTouchHelper.RIGHT ->{
                         Toast.makeText(JobApp.instance.applicationContext, "Swipe Right", Toast.LENGTH_SHORT).show()
                     }
                 }

             }
         }
         val touchHelper = ItemTouchHelper(swipeGesture)
         touchHelper.attachToRecyclerView(mBinding.photoshootCheckListRv)*/

        mBinding.photoshootCheckListRv.setHasFixedSize(true)
    }

    private fun getChecklist(){
        mPhotoshootViewModel.photoshoot_id.observe(viewLifecycleOwner){
            if (!it.isNullOrEmpty()){
                lifecycleScope.launch {
                    val result = PhotoShootRepository.getPhotoShootCheckList(it)
                    when(result){
                        is ResultWrapper.Success ->{
                            val list  = mutableListOf<PhotoShootCheckListDTO>()
                            list.addAll(result.response.map { it })
                            mList.value = list
                        }
                        is ResultWrapper.Failure ->{

                        }
                    }
                }
            }else{
                lifecycleScope.launch {
                    val result = PhotoShootRepository.getPhotoShootCheckList("0")
                    when(result){
                        is ResultWrapper.Success ->{
                            val list  = mutableListOf<PhotoShootCheckListDTO>()
                            list.addAll(result.response.map { it })
                            mList.value = list
                        }
                        is ResultWrapper.Failure ->{

                        }
                    }
                }
            }
        }
    }
    override fun onMessageClick(photoShootCheckListDTO: PhotoShootCheckListDTO) {
        if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
        bottomDialog(photoShootCheckListDTO.category_id, photoShootCheckListDTO.id, photoShootCheckListDTO.title)
         }else{
        showSnackBar("Firstly Create Photoshoot")
          }
    }

    override fun onStatusMessageClick(photoShootCheckListDTO: PhotoShootCheckListDTO) {

               if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
                   lifecycleScope.launch {
                       val result = PhotoShootRepository.checklistcheckedUnchecked(photoShootCheckListDTO.id)
                       when(result){
                           is ResultWrapper.Success ->{
                               photoShootCheckListDTO.status = result.response
                           }
                           is ResultWrapper.Failure ->{

                           }
                       }
                   }
               }else{
                   showSnackBar("Firstly Create Photoshoot")
               }
    }

    override fun onAddChecklistClick(photoShootCheckListDTO: PhotoShootCheckListDTO) {
        if (!mPhotoshootViewModel.photoshoot_id.value.isNullOrEmpty()){
                  bottomDialog(photoShootCheckListDTO.category_id, "", "")
        }else{
            showSnackBar("Firstly Create Photoshoot")
        }
    }
    private fun bottomDialog(category_id: String, checklist_id: String, message : String) {
        bottomSheetDialog = BottomSheetDialog(requireContext(), R.style.SheetDialog)


        val bottomsheetview = LayoutInflater.from(requireContext())
            .inflate(
                R.layout.bottomsheet_add_checklist,
                view?.findViewById(R.id.bottom_sheet)
            ) as ConstraintLayout

        val bottom_sheet_checklist_label = bottomsheetview.findViewById<TextView>(R.id.bottom_sheet_checklist_label)
        val bottom_sheet_checklist_et = bottomsheetview.findViewById<EditText>(R.id.bottom_sheet_checklist_et)
        val bottom_sheet_checklist_saved = bottomsheetview.findViewById<TextView>(R.id.bottom_sheet_checklist_saved)
        val bottom_sheet_checklist_close = bottomsheetview.findViewById<ImageView>(R.id.bottom_sheet_checklist_close)
        val bottom_sheet_checklist_delete = bottomsheetview.findViewById<ImageView>(R.id.bottom_sheet_checklist_delete)

        bottom_sheet_checklist_et.setText(message)
        if (message.isNullOrEmpty()){
            bottom_sheet_checklist_label.setText("Add CheckList Item")
        }else{
            bottom_sheet_checklist_label.setText("Edit CheckList Item")
        }


        bottomSheetDialog.setContentView(bottomsheetview)

        bottomSheetDialog.show()

        bottom_sheet_checklist_delete.setOnClickListener {
            deleteChecklist(category_id,checklist_id)
        }
        bottom_sheet_checklist_close.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottom_sheet_checklist_saved.setOnClickListener {
            if (bottom_sheet_checklist_et.text.isNullOrEmpty()){
                   bottom_sheet_checklist_et.setError("Enter Message")
            }else{
                 addChecklist(category_id,checklist_id, bottom_sheet_checklist_et.text.toString())
                bottomSheetDialog.dismiss()
            }

        }


    }

    private fun deleteChecklist(categoryId: String, checklistId: String) {
        Toast.makeText(JobApp.instance.applicationContext, "Coming soon delete Function", Toast.LENGTH_SHORT).show()

    }

    private fun addChecklist(category_id: String, checklist_id: String,  title: String){
      lifecycleScope.launch {
          val result = PhotoShootRepository.AddEditChecklist(mPhotoshootViewModel.photoshoot_id.value.toString(),
              category_id, checklist_id, title)
          when(result){
              is ResultWrapper.Success ->{
                     getChecklist()
              }
              is ResultWrapper.Failure ->{

              }
          }
      }
   }
}