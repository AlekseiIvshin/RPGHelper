package com.eficksan.rpghelper.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.eficksan.rpghelper.daos.AppDatabase
import com.eficksan.rpghelper.daos.GameSessionDao
import com.eficksan.rpghelper.daos.InventoryDao
import com.eficksan.rpghelper.models.BaseModel
import com.eficksan.rpghelper.models.Item
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.coroutines.CoroutineContext

abstract class SelectableListViewModel<T : BaseModel>(application: Application) : AndroidViewModel(application) {

    companion object {
        const val MODE_DEFAULT = 0
        const val MODE_SELECTION = 1
    }

    private var parentJob = Job()
    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    private val inventoryDao: InventoryDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .inventoryDao()
    private val sessionDao: GameSessionDao =
        Room.databaseBuilder(application, AppDatabase::class.java, "rpg-helper").build()
            .gameSessionDao()

    lateinit var allItems: LiveData<List<T>>
    val selectedItems: MutableLiveData<ArrayList<String>>
    val mode: MutableLiveData<Int>

    init {
        mode = MutableLiveData()
        mode.value = MODE_DEFAULT

        selectedItems = MutableLiveData()
        selectedItems.value = ArrayList()
        selectedItems.observeForever {
            mode.value = when (it.size) {
                0 -> MODE_DEFAULT
                else -> MODE_SELECTION
            }
        }
    }

    protected abstract fun getAll(): LiveData<List<T>>

    protected abstract fun insertInIO(item: T)
    protected abstract fun updateInIO(item: T)
    protected abstract fun deleteInIO(item: T)

    fun insert(item: T) = scope.launch(Dispatchers.IO) {
        insertInIO(item)
    }

    fun update(item: T) = scope.launch(Dispatchers.IO) {
        updateInIO(item)
    }

    fun delete(item: T) = scope.launch(Dispatchers.IO) {
        deleteInIO(item)
    }

    fun deleteSelected() {
        selectedItems.value?.let { selected ->
            allItems.value?.filter { item -> selected.contains(item.uid) }?.forEach { deleteInIO(it) }
        }
    }

    fun selectItem(item: T) {
        selectedItems.value?.let {
            if (it.contains(item.uid)) {
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