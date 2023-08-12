package com.dbvertex.job.peresentation.photoshoot.invoice

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dbvertex.job.databinding.ItemPhotoshootInvoiceItemsBinding
import com.dbvertex.job.network.response.photoshoot.Invoice.InvoiceItemDTO

class InvoiceItemAdapter(val list: List<InvoiceItemDTO>) :
      RecyclerView.Adapter<InvoiceItemAdapter.ItemViewholder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewholder
    {
        val binding = ItemPhotoshootInvoiceItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return   ItemViewholder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewholder, position: Int)
    {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int
    {
        return  list.size
    }
    class ItemViewholder(val binding : ItemPhotoshootInvoiceItemsBinding): RecyclerView.ViewHolder(binding.root)
    {
        fun bind(itemDTO: InvoiceItemDTO) {
            binding.itemPhotoshootInvoiceItemDes.text = "${itemDTO.itemDescription}"
            binding.itemPhotoshootInvoiceItemAmount.text ="Amount       :  ${itemDTO.amount}rs"
            binding.itemPhotoshootInvoiceItemQuantity.text ="Quantity   : ${itemDTO.quantity}"
            binding.itemPhotoshootInvoiceItemGstType.text = "GST Type    :  ${itemDTO.gst_type}"
            binding.itemPhotoshootInvoiceItemRate.text ="Rate          : ${itemDTO.rate} %"
            binding.itemPhotoshootInvoiceItemHsnCode.text ="HSN CODE  :  ${itemDTO.hsncode}"
            binding.itemPhotoshootInvoiceItemFinalAmount.text ="Final Amount : ${itemDTO.final_amount}"
        }

    }
}