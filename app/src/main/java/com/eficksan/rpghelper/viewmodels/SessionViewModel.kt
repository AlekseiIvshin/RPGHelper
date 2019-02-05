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

class SessionViewModel(application: Application): AndroidViewModel(application){

    companion object {
        val MODE_DEFAULT = 0
        val MODE_SELECTION = 1
    }

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob+Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val gameSessionDao: GameSessionDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .gameSessionDao()

    val allSessions: LiveData<List<GameSession>>
    val selectedItems: MutableLiveData<ArrayList<String>>
    val mode: MutableLiveData<Int>

    init {
        allSessions = gameSessionDao.getAll()

        mode = MutableLiveData()
        mode.value = InventoryViewModel.MODE_DEFAULT

        selectedItems = MutableLiveData()
        selectedItems.value = ArrayList()
        selectedItems.observeForever {
            mode.value = when (it.size) {
                0 -> InventoryViewModel.MODE_DEFAULT
                else -> InventoryViewModel.MODE_SELECTION
            }
        }
    }

    fun insert(session: GameSession) = scope.launch(Dispatchers.IO) {
        gameSessionDao.insertAll(session)
    }

    fun delete(session: GameSession) = scope.launch(Dispatchers.IO) {
        gameSessionDao.delete(session)
    }

    fun selectItem(item: GameSession) {
        selectedItems.value?.let {
            if (it.contains(item.uid)){
                it.remove(item.uid)
            } else {
                it.add(item.uid)
            }
            selectedItems.value = selectedItems.value
        }
    }

    fun clearSelection() {
        selectedItems.value?.clear()
        selectedItems.value = selectedItems.value
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun deleteSelected() {
        selectedItems.value?.let { selected ->
            allSessions.value?.filter { item-> selected.contains(item.uid) }?.forEach { delete(it) }
        }
    }

}