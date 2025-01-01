package com.example.tes317

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.tes317.databinding.ActivityDetailBinding

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var appExecutors: AppExecutor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appExecutors = AppExecutor()
        val barangId = intent.getIntExtra("barang_id", -1)
        if (barangId != -1) {
            appExecutors.diskIO.execute {
                val dao = DatabaseBarang.getDatabase(this@DetailActivity).barangDao()
                val selectedBarang = dao.getBarangById(barangId)
                binding.apply {
                    etNama.setText(selectedBarang.nama)
                    etJenis.setText(selectedBarang.jenis)
                    etharga.setText(selectedBarang.harga.toString())
                    btnUpdate.setOnClickListener {
                        val updatedBarang = selectedBarang.copy(
                            nama = etNama.text.toString(),
                            jenis = etJenis.text.toString(),
                            harga = etharga.text.toString().toInt()
                        )
                        appExecutors.diskIO.execute {
                            dao.update(updatedBarang)

                            finish()
                            // Lakukan tindakan update lainnya jika diperlukan
                        }
                    }
                    btnDelete.setOnClickListener {
                        appExecutors.diskIO.execute {
                            dao.delete(selectedBarang)
                            // Lakukan tindakan delete lainnya jika diperlukan
                            finish() // Kembali ke MainActivity setelah menghapus
                        }
                    }
                }
            }
        }
    }
}