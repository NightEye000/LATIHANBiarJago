package com.example.tes317

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.tes317.databinding.ItemBarangBinding

class BarangAdapter(private val barangList: List<Barang>) :
    RecyclerView.Adapter<BarangAdapter.BarangViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BarangViewHolder {
        val binding = ItemBarangBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BarangViewHolder(binding)
    }
    override fun onBindViewHolder(holder: BarangViewHolder, position: Int) {
        val namaBarang = barangList[position]
        holder.bind(namaBarang)
    }
    override fun getItemCount(): Int {
        return barangList.size
    }
    class BarangViewHolder(private val binding: ItemBarangBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(namaBarang: Barang) {
            binding.apply {
                tvNama.text = namaBarang.nama.toString()
                tvJenis.text = namaBarang.jenis.toString()
                itemView.setOnClickListener {
                    val detailIntent = Intent(it.context, DetailActivity::class.java)
                    detailIntent.putExtra("barang_id", namaBarang.id)
                    it.context.startActivity(detailIntent)
                }
            }
        }
    }
}