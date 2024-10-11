package com.dxe.calc.DataBase.MenuDB

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "purity_percentage")
data class PurityPercentageEntry(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val purityPerType: String,
    val purityPerValue: String

)