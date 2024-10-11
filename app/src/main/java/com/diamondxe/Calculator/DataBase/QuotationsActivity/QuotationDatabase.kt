package com.dxe.calc.DataBase.QuotationsActivity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase




@Database(entities = [QuotationModelEntity::class], version = 1)
abstract class QuotationDatabase : RoomDatabase() {
    abstract fun dataModelDao(): QuotationModelDao

    companion object {
        @Volatile
        private var INSTANCE: QuotationDatabase? = null

        fun getDatabase(context: Context): QuotationDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QuotationDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
