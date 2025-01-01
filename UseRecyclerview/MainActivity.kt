package com.example.tes317

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tes317.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dbBarang: DatabaseBarang
    private lateinit var barangDao:BarangDao
    private lateinit var appExecutors: AppExecutor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appExecutors = AppExecutor()
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
                val layoutManager = LinearLayoutManager(this@MainActivity)
                rvRoomDb.layoutManager = layoutManager
                val adapter = BarangAdapter(list)
                rvRoomDb.adapter = adapter
            })
        }
    }
}