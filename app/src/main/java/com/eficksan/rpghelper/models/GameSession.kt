package com.eficksan.rpghelper.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_session")
data class GameSession(
    @PrimaryKey override val uid: String,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "money") var money: Int = 0
): BaseModel