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
import java.util.*
import kotlin.coroutines.CoroutineContext

class ItemUpdateViewModel(application: Application, private val itemUid: String?, sessionId: String?) :
    AndroidViewModel(application) {

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val inventoryDao: InventoryDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .inventoryDao()

    val item: LiveData<Item>

    init {
        if (itemUid==null) {
            item = MutableLiveData()
            item.value = Item(UUID.randomUUID().toString(), "", "", 0.0f, 0, sessionId!!, false)
        } else {
            item = inventoryDao.findById(itemUid)
        }
    }

    fun applyChanges(item: Item) = scope.launch(Dispatchers.IO) {
        if (itemUid==null) {
            inventoryDao.insertAll(item)
        } else {
            inventoryDao.update(item)
        }
    }
}