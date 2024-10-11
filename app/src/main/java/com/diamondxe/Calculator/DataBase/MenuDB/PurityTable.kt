package com.dxe.calc.DataBase.MenuDB

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "purity_table")
data class PurityEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val purityType: String,
    val purityValue: String

)