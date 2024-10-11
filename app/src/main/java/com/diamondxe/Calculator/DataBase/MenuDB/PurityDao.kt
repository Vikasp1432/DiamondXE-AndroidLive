package com.dxe.calc.DataBase.MenuDB

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query


@Dao
interface PurityDao {
    @Insert
    suspend fun insertPurity(purityEntity: PurityEntity)

    @Query("SELECT * FROM purity_table")
    suspend fun getAllPurities(): List<PurityEntity>

    @Query("DELETE FROM purity_table")
    suspend fun deleteAllPurities()

    //insert Value
    @Insert
    suspend fun insertValue(valueEntity: ValueEntity)

    @Query("SELECT value FROM values_table WHERE type = :type")
    suspend fun getValueByType(type: String): String

    @Query("DELETE FROM values_table WHERE type = :type")
    suspend fun deleteValueByType(type: String)

    @Query("DELETE FROM values_table")
    suspend fun deleteallValue()

    //Purity Percentage store

    @Insert
    suspend fun insertpurityPercentage(valueEntity: PurityPercentageEntry)

    @Query("SELECT * FROM purity_percentage")
    suspend fun getAllPuritiesPer(): List<PurityPercentageEntry>

    @Query("SELECT purityPerValue FROM purity_percentage WHERE purityPerType = :type")
    suspend fun getpurityPerValueByType(type: String): String

    @Query("DELETE FROM purity_percentage WHERE purityPerType = :type")
    suspend fun deletepurityPerValueByType(type: String)

    @Query("DELETE FROM purity_percentage")
    suspend fun deleteallpurityPerValue()

}
