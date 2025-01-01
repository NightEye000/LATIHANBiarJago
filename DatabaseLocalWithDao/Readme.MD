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

# BarangDao
package com.example.latihanresponsi

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update


@Dao
interface BarangDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(barang: Barang)

    @Update
    fun update(barang: Barang)

    @Delete
    fun delete(barang: Barang)

    @Query("SELECT *from barang ORDER BY id ASC")
    fun getAllBarang(): LiveData<List<Barang>>

    @Query("SELECT *from barang WHERE id= :barangId")
    fun getBarangById(barangId: Int): Barang
}


# Barang
package com.example.latihanresponsi

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class Barang(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name="id")
    var id:Int = 0,

    @ColumnInfo(name = "nama")
    var nama:String? = null,

    @ColumnInfo(name = "jenis")
    var jenis:String? = null,

    @ColumnInfo(name = "harga")
    var harga:Int? = null,
)

# DatabaseBarang
package com.example.latihanresponsi

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Barang::class], version = 1, exportSchema = false)
abstract class DatabaseBarang : RoomDatabase() {
    abstract fun barangDao(): BarangDao
    companion object {
        @Volatile
        private var INSTANCE: DatabaseBarang? = null
        fun getDatabase(context: Context): DatabaseBarang {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    DatabaseBarang::class.java,
                    "db_barang"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

# DetailActivity
package com.example.latihanresponsi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.latihanresponsi.databinding.ActivityDetailBinding

class DetailActivity  : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    private lateinit var appExecutors: AppExecutor
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        appExecutors = AppExecutor
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

