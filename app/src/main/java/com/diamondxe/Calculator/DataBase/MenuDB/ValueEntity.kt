package com.dxe.calc.DataBase.MenuDB

import androidx.room.Entity
import androidx.room.PrimaryKey



@Entity(tableName = "values_table")
data class ValueEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val type: String,
    val value: String
)