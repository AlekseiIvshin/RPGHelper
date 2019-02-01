package com.eficksan.rpghelper.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import com.eficksan.rpghelper.models.Item

@Dao
interface  InventoryDao {

    @Query("select * from inventory where session_uid LIKE :sessionUid")
    fun getAll(sessionUid: String): LiveData<List<Item>>

    @Insert
    fun insertAll(vararg items: Item)

    @Delete
    fun delete(item: Item)

    @Update
    fun  update(item: Item)
}