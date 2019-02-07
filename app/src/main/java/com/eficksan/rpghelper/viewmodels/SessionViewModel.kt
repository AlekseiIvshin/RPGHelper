package com.eficksan.rpghelper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.eficksan.rpghelper.daos.AppDatabase
import com.eficksan.rpghelper.daos.GameSessionDao
import com.eficksan.rpghelper.models.GameSession
import com.eficksan.rpghelper.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

class SessionViewModel(application: Application): SelectableListViewModel<GameSession>(application){

    private val gameSessionDao: GameSessionDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .gameSessionDao()

    init {
        allItems = gameSessionDao.getAll()
    }

    override fun insertInIO(item: GameSession) {
        gameSessionDao.insertAll(item)
    }

    override fun updateInIO(item: GameSession) {
        gameSessionDao.update(item)
    }

    override fun deleteInIO(item: GameSession) {
        gameSessionDao.delete(item)
    }

}