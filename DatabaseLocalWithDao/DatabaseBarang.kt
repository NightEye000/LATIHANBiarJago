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
