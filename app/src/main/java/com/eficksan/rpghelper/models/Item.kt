package com.eficksan.rpghelper.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "inventory",
    foreignKeys = [ForeignKey(
        entity = GameSession::class,
        parentColumns = ["uid"],
        childColumns = ["session_uid"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Item(
    @PrimaryKey override val uid: String,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "weight") val weight: Float,
    @ColumnInfo(name = "cost") val cost: Int,
    @ColumnInfo(name = "session_uid") val sessionUid: String,
    @ColumnInfo(name = "is_equipped") var isEquipped: Boolean
): BaseModel