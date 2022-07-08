package com.testing.myapplication.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.testing.myapplication.databinding.ItemUpdateAdapterBinding
import com.testing.myapplication.response.product.Data

class AdapterHome constructor(
    val list: List<Data>,
    val context: Context,
    private val itemAdapterCallback: ItemAdapterCallback
) : RecyclerView.Adapter<AdapterHome.MyViewHolder>() {

    var listenerDelete = { _:Int-> }

    inner class MyViewHolder(
        val binding: ItemUpdateAdapterBinding,
        val context: Context
    ) : RecyclerView.ViewHolder(binding.root){
        @SuppressLint("SetTextI18n")
        fun bind(position: Int){

            Glide.with(context)
                .load(list[position].photoWarung)
                .into(binding.ivProduct)

            binding.tvNameWarung.text = list[position].nameWarung
            binding.tvAddress.text = list[position].alamatWarung
            binding.tvLatLong.text = "${list[position].lat}, ${list[position].long}"

            binding.ivTrash.setOnClickListener {
                listenerDelete.invoke(position)
            }

            itemView.setOnClickListener {
                itemAdapterCallback.onClick(it, list[position])
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUpdateAdapterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyViewHolder(binding, parent.context)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(position)
    }

    fun setOnDeleteListener(listener:(Int)->Unit){
        this.listenerDelete = listener
    }


    override fun getItemCount(): Int = list.size

    interface ItemAdapterCallback {
        fun onClick(view: View, data: Data)
    }
}