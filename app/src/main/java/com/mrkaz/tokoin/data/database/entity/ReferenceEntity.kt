package com.mrkaz.tokoin.data.database.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "references")
data class ReferenceEntity(
    @PrimaryKey @ColumnInfo(name = "reference") val reference: String
)