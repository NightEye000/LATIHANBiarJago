# MainActivity
package com.example.latihanresponsi

import android.R
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.example.latihanresponsi.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbBarang: DatabaseBarang
    private lateinit var barangDao:BarangDao
    private lateinit var appExecutors: AppExecutor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appExecutors = AppExecutor
        dbBarang = DatabaseBarang.getDatabase(applicationContext)
        barangDao = dbBarang.barangDao()
        binding.apply {
            fabAdd.setOnClickListener {
                appExecutors.diskIO.execute {
                    val barangTitles = listOf("Meja", "Semen", "Triplek", "Pasir")
                    val jenisBarang = listOf("Perabotan", "Material", "Material", "Material")
                    val hargaBarang = listOf(50000,48000,15000,68000)
                    for(i in 1..4){
                        val newBarang = Barang(i,barangTitles[i-1],jenisBarang[i-1],hargaBarang[i-1])
                        barangDao.insert(newBarang)
                    }
                }
            }
            val barangList: LiveData<List<Barang>> = barangDao.getAllBarang()
            barangList.observe(this@MainActivity, Observer { list ->
                val namaBarangList = list.map { it.nama }
                lvRoomDb.adapter = ArrayAdapter(this@MainActivity,
                    R.layout.simple_list_item_1, namaBarangList)
                lvRoomDb.setOnItemClickListener { _, _, position, _ ->
                    val selectedBarang = list[position]
                    // Dapatkan ID atau data lain yang perlu dikirim ke halaman detail
                    val detailIntent = Intent(this@MainActivity, DetailActivity::class.java)
                    detailIntent.putExtra("barang_id", selectedBarang.id) // Contoh: Kirim ID barang
                    startActivity(detailIntent)
                }
            })
        }
    }
}

