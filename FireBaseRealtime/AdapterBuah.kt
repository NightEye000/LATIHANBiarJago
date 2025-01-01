package com.example.pertemuan8_ajah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pertemuan8_ajah.databinding.ItemBuahBinding

class AdapterBuah(private val data2: List<ModelBuah>) :
    RecyclerView.Adapter<AdapterBuah.BuahViewHolder>() {
    class BuahViewHolder(private val binding: ItemBuahBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: ModelBuah) {
            binding.tvNama.text = employee.nama
            binding.tvHarga.text = employee.harga
            Glide.with(binding.root.context)
                .load(employee.photoUrl)
                .centerCrop()
                .into(binding.ivPhotos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuahViewHolder {
        val binding = ItemBuahBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return BuahViewHolder(binding)
    }

    override fun onBindViewHolder(holder: BuahViewHolder, position: Int) {
        val data2 = data2[position]
        holder.bind(data2)
    }

    override fun getItemCount(): Int = data2.size
}