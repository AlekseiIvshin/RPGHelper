package com.eficksan.rpghelper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.eficksan.rpghelper.daos.AppDatabase
import com.eficksan.rpghelper.daos.GameSessionDao
import com.eficksan.rpghelper.daos.InventoryDao
import com.eficksan.rpghelper.models.GameSession
import com.eficksan.rpghelper.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

class InventoryViewModel(application: Application, val sessionUid: String): SelectableListViewModel<Item>(application){

    private val inventoryDao: InventoryDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .inventoryDao()
    private val sessionDao: GameSessionDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .gameSessionDao()

    var money: MutableLiveData<Int>

    init {
        allItems = inventoryDao.getAll(sessionUid)
        money = MutableLiveData()
        sessionDao.findById(sessionUid).observeForever { money.value = it.money }
    }

    override fun getAll(): LiveData<List<Item>> = inventoryDao.getAll(sessionUid)

    override fun insertInIO(item: Item) {
        inventoryDao.insertAll(item)
    }

    override fun updateInIO(item: Item) {
        inventoryDao.update(item)
    }

    override fun deleteInIO(item: Item) {
        inventoryDao.delete(item)
    }

    fun equipOrTakeOffSelected() {
        selectedItems.value?.let { selected ->
            val selectedItemsList = allItems.value?.filter { item-> selected.contains(item.uid) }
            val shouldEquip = selectedItemsList!!.count { it.isEquipped } == 0
            selectedItemsList.forEach {
                it.isEquipped = shouldEquip
                update(it)
            }
        }
    }
}