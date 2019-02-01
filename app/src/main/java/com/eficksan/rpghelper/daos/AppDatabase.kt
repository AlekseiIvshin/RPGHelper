package com.eficksan.rpghelper.daos

import androidx.room.Database
import androidx.room.RoomDatabase
import com.eficksan.rpghelper.models.GameSession
import com.eficksan.rpghelper.models.Item

@Database(entities = [GameSession::class, Item::class], version = 1)
abstract  class  AppDatabase: RoomDatabase() {

    abstract fun gameSessionDao(): GameSessionDao

    abstract fun inventoryDao(): InventoryDao
}