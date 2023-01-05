package com.redmechax00.astonintensive6.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.redmechax00.astonintensive6.data.model.ContactModel

@Database(entities = [ContactModel::class], version = 1, exportSchema = false)
abstract class ContactDatabase : RoomDatabase() {
    companion object {
        const val CONTACTS_DATABASE = "database-contacts"
    }

    abstract fun getContactDao(): ContactDao
}