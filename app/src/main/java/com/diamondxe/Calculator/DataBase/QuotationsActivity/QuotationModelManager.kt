package com.dxe.calc.DataBase.QuotationsActivity

import android.util.Log


class QuotationModelManager(private val dao: QuotationModelDao) {
    private val maxSize = 10

    /*suspend fun addDataModel(dataModel: QuotationModelEntity) {
        val currentCount = dao.getCount()
        Log.e("dataModelCount", "Total count before insert: $currentCount")

        dao.insertAndLimit(dataModel, maxSize)
        Log.e("dataModel", "Inserted new data with ID: ${dataModel.id}")

        val dataModelCount = dao.getCount()
        Log.e("dataModelCount", "Total count after insert: $dataModelCount")

        if (dataModelCount > maxSize) {
            val oldestDataModel = dao.getOldestDataModel()

            if (oldestDataModel != null) {
                Log.e("dataModel", "Deleting oldest entry with ID: ${oldestDataModel.id}")
                dao.delete(oldestDataModel)

                val updatedCount = dao.getCount()
                Log.e("dataModelCount", "Total count after deletion: $updatedCount")
            }
        }
    }*/
    suspend fun addDataModel(dataModel: QuotationModelEntity): Long {
        val currentCount = dao.getCount()
        //Log.e("dataModelCount", "Total count before insert: $currentCount")

        // Insert the data model and get the inserted ID
        val insertedId = dao.insertAndLimit(dataModel, maxSize)
       // Log.e("dataModel", "Inserted new data with ID: $insertedId")

        val dataModelCount = dao.getCount()
       // Log.e("dataModelCount", "Total count after insert: $dataModelCount")

        if (dataModelCount > maxSize) {
            val oldestDataModel = dao.getOldestDataModel()

            if (oldestDataModel != null) {
                //Log.e("dataModel", "Deleting oldest entry with ID: ${oldestDataModel.id}")
                dao.delete(oldestDataModel)

                val updatedCount = dao.getCount()
               // Log.e("dataModelCount", "Total count after deletion: $updatedCount")
            }
        }

        // Return the auto-generated ID of the inserted entity
        return insertedId
    }


    suspend fun getDataModelByItemName(itemName: String): QuotationModelEntity? {
        return dao.getDataModelByItemName(itemName)
    }

    suspend fun getDataModelById(id: Long): QuotationModelEntity? {
        return dao.getDataModelById(id)
    }
    suspend fun updateDataModel(newFieldValue: QuotationModelEntity) {
        dao.update( newFieldValue)
    }

    suspend fun getDataModels(): List<QuotationModelEntity> {
        return dao.getAllDataModels()
    }
}