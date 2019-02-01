package com.eficksan.rpghelper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.eficksan.rpghelper.daos.AppDatabase
import com.eficksan.rpghelper.daos.InventoryDao
import com.eficksan.rpghelper.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class InventoryViewModel(application: Application, val sessionUid: String): AndroidViewModel(application){

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
    get() = parentJob+ Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val inventoryDao: InventoryDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .inventoryDao()

    val allItems: LiveData<List<Item>>

    init {
        allItems = inventoryDao.getAll(sessionUid)
    }

    fun insert(item: Item) = scope.launch(Dispatchers.IO) {
        inventoryDao.insertAll(item)
    }

    fun delete(item: Item) = scope.launch(Dispatchers.IO) {
        inventoryDao.delete(item)
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }
}