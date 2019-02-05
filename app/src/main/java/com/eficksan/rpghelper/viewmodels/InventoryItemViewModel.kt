package com.eficksan.rpghelper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.eficksan.rpghelper.daos.AppDatabase
import com.eficksan.rpghelper.daos.GameSessionDao
import com.eficksan.rpghelper.daos.InventoryDao
import com.eficksan.rpghelper.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

class InventoryItemViewModel(application: Application, itemUid: String): AndroidViewModel(application){

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob+ Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val inventoryDao: InventoryDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .inventoryDao()

    val item: LiveData<Item>

    init {
        item = inventoryDao.findById(itemUid)
    }

    fun update(item: Item) = scope.launch(Dispatchers.IO) {
        inventoryDao.update(item)
    }

    fun delete(item: Item) = scope.launch(Dispatchers.IO) {
        inventoryDao.delete(item)
    }

    fun deleteCurrent() {
        item.value?.let {
            delete(it)
        }
    }

    fun equipOrTakeOffCurrent() {
        item.value?.let {
            it.isEquipped = !it.isEquipped
            update(it)
        }
    }
}