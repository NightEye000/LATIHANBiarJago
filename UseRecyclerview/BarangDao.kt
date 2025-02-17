package com.example.tes317

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

    @Query("SELECT * FROM barang ORDER BY id ASC")
    fun getAllBarang(): LiveData<List<Barang>>

    @Query("SELECT * FROM barang WHERE id = :id")
    fun getBarangById(id: Int): Barang
}