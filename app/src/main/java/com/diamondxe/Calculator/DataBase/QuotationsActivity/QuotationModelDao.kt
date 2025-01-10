package com.dxe.calc.DataBase.QuotationsActivity

import android.util.Log
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update


@Dao
interface QuotationModelDao {

    @Insert
    suspend fun insert(dataModel: QuotationModelEntity): Long

    @Query("SELECT * FROM data_model")
    suspend fun getAllDataModels(): List<QuotationModelEntity>

    @Query("SELECT COUNT(*) FROM data_model")
    suspend fun getCount(): Int



    @Update
    suspend fun update(dataModel: QuotationModelEntity)

    @Query("SELECT * FROM data_model WHERE id = :id LIMIT 1")
    suspend fun getDataModelByItemName(id: String): QuotationModelEntity?

    @Query("SELECT * FROM data_model WHERE id = :id LIMIT 1")
    suspend fun getDataModelById(id: Long): QuotationModelEntity?


    @Query("SELECT * FROM data_model ORDER BY id ASC LIMIT 1")
    suspend fun getOldestDataModel(): QuotationModelEntity?

    @Delete
    suspend fun delete(dataModel: QuotationModelEntity)


    /*@Transaction
    suspend fun insertAndLimit(dataModel: QuotationModelEntity, maxSize: Int) {
        val insertedId = insert(dataModel)
        Log.e("insert", "Inserted new data with ID: $insertedId")

        val count = getCount()
        Log.e("dataModelCount", "Total count after insert: $count")

        if (count > maxSize) {
            val oldestDataModel = getOldestDataModel()
            if (oldestDataModel != null) {
                Log.e("dataModel", "Deleting oldest entry with ID: ${oldestDataModel.id}")
                delete(oldestDataModel)
            }
        }
    }*/
    @Transaction
    suspend fun insertAndLimit(dataModel: QuotationModelEntity, maxSize: Int): Long {
        // Insert the new data and get the auto-generated ID
        val insertedId = insert(dataModel)
       // Log.e("insert", "Inserted new data with ID: $insertedId")

        // Get the total count after insertion
        val count = getCount()
        //Log.e("dataModelCount", "Total count after insert: $count")

        // If the total count exceeds the maximum size, delete the oldest entry
        if (count > maxSize) {
            val oldestDataModel = getOldestDataModel()
            if (oldestDataModel != null) {
              //  Log.e("dataModel", "Deleting oldest entry with ID: ${oldestDataModel.id}")
                delete(oldestDataModel)
            }
        }

        return insertedId
    }

}