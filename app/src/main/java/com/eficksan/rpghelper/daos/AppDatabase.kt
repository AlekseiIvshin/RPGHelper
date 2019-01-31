package com.eficksan.rpghelper.daos

import android.content.Context
import androidx.room.Database
import androidx.room.RoomDatabase
import com.eficksan.rpghelper.models.GameSession

@Database(entities = arrayOf(GameSession::class), version = 1)
abstract  class  AppDatabase: RoomDatabase() {

    abstract fun gameSessionDao(): GameSessionDao
}