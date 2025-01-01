package com.example.pertemuan8_ajah

import android.app.ProgressDialog
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.pertemuan8_ajah.databinding.ActivityBuahBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class BuahActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBuahBinding
    private var url2: String=""
    private var name: String=""
    private lateinit var storage: StorageReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBuahBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.rvBuah.layoutManager = LinearLayoutManager(this)
        binding.rvBuah.setHasFixedSize(true)
        showData()
        tambahData()
    }
    private fun showData() {
        val dataRef = FirebaseDatabase.getInstance().getReference("Buah")
        dataRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val BuahList = mutableListOf<ModelBuah>()
                val adapter = AdapterBuah(BuahList)
                for (dataSnapshot in snapshot.children) {
                    val pekerjaanKey = dataSnapshot.getValue(ModelBuah::class.java)
                    pekerjaanKey?.let {
                        BuahList.add(it)
                        Log.d("cek data firebase", it.toString())
                    }
                }
                if (BuahList.isNotEmpty()) {
                    binding.rvBuah.adapter = adapter
                } else {
                    Toast.makeText(this@BuahActivity, "Data not found",
                        Toast.LENGTH_LONG).show()
                }
            }
            override fun onCancelled(snapshot: DatabaseError) {
// Handle onCancelled
            }
        })
    }
    private fun tambahData(){
        val database: FirebaseDatabase = FirebaseDatabase.getInstance()
        val pekerjaanRef: DatabaseReference = database.getReference("Buah")
        binding.fabAddBuah.setOnClickListener {
            val dialogView = LayoutInflater.from(this).inflate(R.layout.upload_dialog, null)
            MaterialAlertDialogBuilder(this)
                .setTitle("Tambah Buah")
                .setView(dialogView)
                .setPositiveButton("Tambah") { dialog, _ ->
                    val jobName =
                        dialogView.findViewById<EditText>(R.id.editTextJobName).text.toString()
                    val shift = dialogView.findViewById<EditText>(R.id.editTextShift).text.toString()
                    val pekerjaanData = HashMap<String, Any>()
                    pekerjaanData["nama"] = jobName
                    pekerjaanData["harga"] = shift
                    pekerjaanData["photoUrl"] = url2
                    name = jobName
                    val newPekerjaanRef = pekerjaanRef.push()
                    newPekerjaanRef.setValue(pekerjaanData)
                    dialog.dismiss()
                }
                .setNegativeButton("Batal") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
            dialogView.findViewById<Button>(R.id.btnUpload).setOnClickListener {
                choosePicture()
            }
        }
    }
    private val getMultipleContentsPicture =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null){
                uploadPic(uri)
            }
        }
    private fun choosePicture() {
        getMultipleContentsPicture.launch("image/*")
    }
    private fun getFileName(uri: Uri): String {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor.use {
            if (it != null && it.moveToFirst()) {
                val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (nameIndex != -1) {
                    return it.getString(nameIndex)
                }
            }
        }
        return System.currentTimeMillis().toString()
    }
    private fun uploadPic(uris: Uri) {
        val selectedFile = mutableListOf<String>()
        val progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Uploading Picture...")
        progressDialog.setCancelable(false)
        progressDialog.show()
        storage = FirebaseStorage.getInstance().getReference("Buah")
        val fileName = getFileName(uris)
        selectedFile.add(fileName)
        val contentResolver = this.contentResolver
        val nama = name
// Upload semua file ke Firebase Storage
        val picRef = storage.child("image/${nama}/${uris.lastPathSegment}")
        try {
            val inputStream = contentResolver.openInputStream(uris)
            picRef.putStream(inputStream!!)
                .addOnSuccessListener { taskSnapshot ->
// Upload berhasil
                    progressDialog.dismiss()
                    picRef.downloadUrl.addOnSuccessListener { uri ->
                        val downloadUrl = uri.toString()
                        url2 = downloadUrl
                    }.addOnFailureListener {
                    }
                    Log.d("TAG", "File uploaded to Firebase Storage")
                }
                .addOnFailureListener { exception ->
                    Log.e("TAG", "Upload failed", exception)
                }
        } catch (e: Exception) {
            Log.e("TAG", "Failed to open input stream", e)
        }
    }
}