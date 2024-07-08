package com.ahmedwagdy.testapi.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ahmedwagdy.testapi.databinding.HeaderItemLayoutBinding
import com.ahmedwagdy.testapi.ui.data.HeaderItem

class HeaderAdapter(private var headers: MutableList<HeaderItem>) : RecyclerView.Adapter<HeaderAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: HeaderItemLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: HeaderItem, position: Int) {
            binding.itemKey.text = item.key
            binding.itemValue.text = item.value
            binding.removeImg.setOnClickListener {
                headers.removeAt(position)
                notifyDataSetChanged()
            }
        }
    }

    fun submitHeader(headers: MutableList<HeaderItem>){
        this.headers = headers
        notifyDataSetChanged()
    }
    fun getHeaders() = headers

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = HeaderItemLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(headers[position],position)
    }

    override fun getItemCount() = headers.size
}
