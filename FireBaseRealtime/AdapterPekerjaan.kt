package com.example.pertemuan8_ajah

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.pertemuan8_ajah.databinding.ItemPekerjaanBinding

class AdapterPekerjaan(private val data: List<ModelPekerjaan>) :
    RecyclerView.Adapter<AdapterPekerjaan.PekerjaanViewHolder>() {
    class PekerjaanViewHolder(private val binding: ItemPekerjaanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(employee: ModelPekerjaan) {
            binding.tvNama.text = employee.nama
            binding.tvShift.text = employee.shift
            Glide.with(binding.root.context)
                .load(employee.photoUrl)
                .centerCrop()
                .into(binding.ivPhotos)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PekerjaanViewHolder {
        val binding = ItemPekerjaanBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return PekerjaanViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PekerjaanViewHolder, position: Int) {
        val data = data[position]
        holder.bind(data)
    }

    override fun getItemCount(): Int = data.size
}