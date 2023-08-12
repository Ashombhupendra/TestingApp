package com.dbvertex.job.peresentation.photoshoot.contract

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemPhotoshootContractBinding
import com.dbvertex.job.network.response.photoshoot.ContractDTO

class Contract_adapter (val list : List<ContractDTO>, val onContractClick : onContractClick) :
RecyclerView.Adapter<Contract_adapter.ContractViewHolder>(){

    class ContractViewHolder(val mBinding : ItemPhotoshootContractBinding) : RecyclerView.ViewHolder(mBinding.root) {
             fun ConBind(contractDTO: ContractDTO, onContractClick: onContractClick) {
               mBinding.contractDescription.text = "${contractDTO.content}"

               mBinding.arrow.setOnClickListener {
                   onContractClick.NavtoContractDetail(contractDTO)
               }
                 mBinding.photoshootContractPreview.setOnClickListener {
                     onContractClick.PreviewCOntract(contractDTO)
                 }
                 mBinding.photoshootContractShare.setOnClickListener {
                     onContractClick.SharetoContract(contractDTO)
                 }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContractViewHolder {
        val mBinding = ItemPhotoshootContractBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ContractViewHolder(mBinding)
    }

    override fun onBindViewHolder(holder: ContractViewHolder, position: Int) {
         holder.ConBind(list[position], onContractClick)
    }

    override fun getItemCount(): Int {
       return list.size
    }

}

interface  onContractClick{
    fun NavtoContractDetail(contractDTO: ContractDTO)
    fun PreviewCOntract(contractDTO: ContractDTO)
    fun SharetoContract(contractDTO: ContractDTO)
}