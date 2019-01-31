package com.eficksan.rpghelper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.eficksan.rpghelper.daos.AppDatabase
import com.eficksan.rpghelper.daos.GameSessionDao
import com.eficksan.rpghelper.models.GameSession
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class SessionViewModel(application: Application): AndroidViewModel(application){

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob+Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val gameSessionDao: GameSessionDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .gameSessionDao()

    val allSessions: LiveData<List<GameSession>>

    init {
        allSessions = gameSessionDao.getAll()
    }

    fun insert(session: GameSession) = scope.launch(Dispatchers.IO) {
        gameSessionDao.insertAll(session)
    }

    fun delete(session: GameSession) = scope.launch(Dispatchers.IO) {
        gameSessionDao.delete(session)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}