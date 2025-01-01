package com.example.pertemuan8_ajah

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.pertemuan8_ajah.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnPekerjaan.setOnClickListener {
            val intent = Intent(this, PekerjaanActivity::class.java)
            startActivity(intent)
        }
        binding.btnBuah.setOnClickListener {
            val intent = Intent(this, BuahActivity::class.java)
            startActivity(intent)
        }
    }

}