package com.redmechax00.astonintensive6.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query
import androidx.room.OnConflictStrategy
import com.redmechax00.astonintensive6.data.model.ContactModel

@Dao
interface ContactDao {
    @Update
    fun update(contact: ContactModel)

    @Delete
    fun delete(contact: ContactModel)

    @Query("SELECT * FROM ContactModel")
    fun getAll(): MutableList<ContactModel>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(contacts: List<ContactModel>)
}