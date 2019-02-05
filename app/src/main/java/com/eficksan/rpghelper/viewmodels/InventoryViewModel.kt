package com.eficksan.rpghelper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.eficksan.rpghelper.daos.AppDatabase
import com.eficksan.rpghelper.daos.InventoryDao
import com.eficksan.rpghelper.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

class InventoryViewModel(application: Application, val sessionUid: String): AndroidViewModel(application){

    companion object {
        val MODE_DEFAULT = 0
        val MODE_SELECTION = 1
    }

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
    get() = parentJob+ Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val inventoryDao: InventoryDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .inventoryDao()

    val allItems: LiveData<List<Item>>
    val selectedItems: MutableLiveData<ArrayList<Item>>
    val mode: MutableLiveData<Int>

    init {
        allItems = inventoryDao.getAll(sessionUid)
        selectedItems = MutableLiveData()
        selectedItems.value = ArrayList()
        mode = MutableLiveData()
        mode.value = MODE_DEFAULT

        selectedItems.observeForever {
            mode.value = when (it.size) {
                0 -> MODE_DEFAULT
                else -> MODE_SELECTION
            }
        }
    }

    fun insert(item: Item) = scope.launch(Dispatchers.IO) {
        inventoryDao.insertAll(item)
    }

    fun delete(item: Item) = scope.launch(Dispatchers.IO) {
        inventoryDao.delete(item)
    }

    fun selectItem(item: Item) {
        selectedItems.value?.let {
            if (it.contains(item)){
                it.remove(item)
            } else {
                it.add(item)
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
}