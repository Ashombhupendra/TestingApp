package com.dbvertex.job.peresentation.inspiration

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.dbvertex.job.JobApp
import com.dbvertex.job.databinding.FragmentInspirationBinding
import com.dbvertex.job.network.response.inspiration.InspirationDTO
import kotlinx.coroutines.Job


import me.everything.android.ui.overscroll.VerticalOverScrollBounceEffectDecorator
import me.everything.android.ui.overscroll.adapters.RecyclerViewOverScrollDecorAdapter


class InspirationFrag : Fragment(),onInpirationalCLick {

        private lateinit var mBinding : FragmentInspirationBinding
        private val mInspirationViewmodel by activityViewModels<InspirationViewmodel>()
        private lateinit var adapter : InspirationAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ):View {
        mBinding = FragmentInspirationBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@InspirationFrag

        }
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mInspirationViewmodel.getInspirationsList()
        mInspirationViewmodel.inspirationlist.observe(viewLifecycleOwner, Observer {
             adapter = InspirationAdapter(JobApp.instance.applicationContext, it as ArrayList<InspirationDTO>, this)
           mBinding.inspirationRv.adapter= adapter
        })


        VerticalOverScrollBounceEffectDecorator(RecyclerViewOverScrollDecorAdapter(mBinding.inspirationRv))

    }

    override fun onInspirationlikeClick(inspirationDTO: InspirationDTO) {
        mInspirationViewmodel.setlikeUnlike(inspirationDTO.id)
    }

    override fun onInspirationCommentClick(inspirationDTO: InspirationDTO) {
        Toast.makeText(JobApp.instance.applicationContext, "comment", Toast.LENGTH_SHORT).show()
    }

    override fun onInspirationFavClick(inspirationDTO: InspirationDTO) {
        mInspirationViewmodel.setFavUnFav(inspirationDTO.id)
    }

    override fun onInspirationDetail(inspirationDTO: InspirationDTO, postion : Int) {
     //  showSnackBar("Hello ${inspirationDTO.id},\n adapter position : $postion")
     /*   mInspirationViewmodel.getSingleInspiration(inpiration_id = inspirationDTO.id)
        findNavController().navigate(R.id.inspirationDetail)*/

        if (!inspirationDTO.user_liked){
            mInspirationViewmodel.setlikeUnlike(inspirationDTO.id)
            inspirationDTO.user_liked = true
            adapter.notifyItemChanged(postion)

        }
    }


}