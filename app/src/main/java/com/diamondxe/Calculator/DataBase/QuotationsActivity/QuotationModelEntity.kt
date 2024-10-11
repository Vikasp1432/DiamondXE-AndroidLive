package com.dxe.calc.DataBase.QuotationsActivity

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "data_model")
data class QuotationModelEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    var metalWeight: String,
    var metalRate: String,
    var metalOutput: String,
    var labourWeight: String,
    var labourRate: String,
    var labourOutput: String,
    var solitaireWeight: String,
    var solitaireRate: String,
    var solitaireOutput: String,
    var sideWeight: String,
    var sideRate: String,
    var sideOutput: String,
    var colStoneWeight: String,
    var colStoneRate: String,
    var colStoneOutput: String,
    var charges: String,
    var tax: String,
    var totalPrice: String,
    var currentdatetime: String,
    var caret: String,
    var itemName: String,
    var radiobuttonName:String,
    var chargeTxt:String,
    var solitairetxt:String,
    var sidediatxt:String

): Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong() ?: 0,
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?:"",
        parcel.readString() ?:"",
        parcel.readString() ?:"",
        parcel.readString() ?:""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(metalWeight)
        parcel.writeString(metalRate)
        parcel.writeString(metalOutput)
        parcel.writeString(labourWeight)
        parcel.writeString(labourRate)
        parcel.writeString(labourOutput)
        parcel.writeString(solitaireWeight)
        parcel.writeString(solitaireRate)
        parcel.writeString(solitaireOutput)
        parcel.writeString(sideWeight)
        parcel.writeString(sideRate)
        parcel.writeString(sideOutput)
        parcel.writeString(colStoneWeight)
        parcel.writeString(colStoneRate)
        parcel.writeString(colStoneOutput)
        parcel.writeString(charges)
        parcel.writeString(tax)
        parcel.writeString(totalPrice)
        parcel.writeString(currentdatetime)
        parcel.writeString(caret)
        parcel.writeString(itemName)
        parcel.writeString(radiobuttonName)
        parcel.writeString(chargeTxt)
        parcel.writeString(solitairetxt)
        parcel.writeString(sidediatxt)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<QuotationModelEntity> {
        override fun createFromParcel(parcel: Parcel): QuotationModelEntity {
            return QuotationModelEntity(parcel)
        }

        override fun newArray(size: Int): Array<QuotationModelEntity?> {
            return arrayOfNulls(size)
        }
    }
}
