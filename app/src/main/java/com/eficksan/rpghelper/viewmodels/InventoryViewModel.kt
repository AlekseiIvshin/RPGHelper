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
    private val sessionDao: GameSessionDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .gameSessionDao()

    val allItems: LiveData<List<Item>>
    val selectedItems: MutableLiveData<ArrayList<String>>
    val mode: MutableLiveData<Int>
    var money: MutableLiveData<Int>

    init {
        allItems = inventoryDao.getAll(sessionUid)

        mode = MutableLiveData()
        mode.value = MODE_DEFAULT

        money = MutableLiveData()
        sessionDao.findById(sessionUid).observeForever { money.value = it.money }

        selectedItems = MutableLiveData()
        selectedItems.value = ArrayList()
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

    fun update(item: Item) = scope.launch(Dispatchers.IO) {
        inventoryDao.update(item)
    }

    fun delete(item: Item) = scope.launch(Dispatchers.IO) {
        inventoryDao.delete(item)
    }

    fun deleteSelected() {
        selectedItems.value?.let { selected ->
            allItems.value?.filter { item-> selected.contains(item.uid) }?.forEach { delete(it) }
        }
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

    fun selectItem(item: Item) {
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
}