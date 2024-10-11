package com.dxe.calc.DataBase.MenuDB

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [PurityEntity::class, ValueEntity::class, PurityPercentageEntry::class], version = 1, exportSchema = false)
abstract class PurityDatabase : RoomDatabase() {
    abstract fun purityDao(): PurityDao

    companion object {
        @Volatile
        private var INSTANCE: PurityDatabase? = null

        fun getDatabase(context: Context): PurityDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    PurityDatabase::class.java,
                    "purity_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}