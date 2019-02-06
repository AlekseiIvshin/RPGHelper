package com.eficksan.rpghelper.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eficksan.rpghelper.models.GameSession

@Dao
interface  GameSessionDao{
    @Query("select * from game_session")
    fun getAll(): LiveData<List<GameSession>>

    @Query("select * from game_session where uid like :uid")
    fun findById(uid: String): LiveData<GameSession>

    @Insert
    fun insertAll(vararg session: GameSession)

    @Update
    fun update(session: GameSession)

    @Delete
    fun delete(gameSession: GameSession)
}