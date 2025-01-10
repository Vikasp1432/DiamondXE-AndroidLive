package com.dxe.calc.DataBase.QuotationsActivity

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [QuotationModelEntity::class], version = 2)
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
                ).addMigrations(MIGRATION_1_2).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}

/*.addMigrations(MIGRATION_1_2)*/

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        // Add new columns
        database.execSQL("ALTER TABLE data_model ADD COLUMN currencyCode TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE data_model ADD COLUMN currencysymbol TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE data_model ADD COLUMN currencyvalue TEXT NOT NULL DEFAULT ''")
        database.execSQL("ALTER TABLE data_model ADD COLUMN currencyvalue TEXT NOT NULL DEFAULT ''")
    }
}


/*@Database(entities = [QuotationModelEntity::class], version = 1)
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
}*/
