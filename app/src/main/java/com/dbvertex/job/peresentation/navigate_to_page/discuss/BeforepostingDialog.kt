package com.dbvertex.job.peresentation.navigate_to_page.discuss

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.dbvertex.job.databinding.FragmentBeforepostingDialogBinding


class BeforepostingDialog : Fragment() {

      lateinit var mBinding : FragmentBeforepostingDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = FragmentBeforepostingDialogBinding.inflate(layoutInflater, container, false).apply {
            lifecycleOwner = this@BeforepostingDialog

        }
        return mBinding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

           mBinding.cancelBeforposting.setOnClickListener {
               findNavController().navigateUp()
           }
    }

}