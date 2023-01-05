package com.redmechax00.astonintensive6.data

import com.redmechax00.astonintensive6.data.local.ContactDao
import com.redmechax00.astonintensive6.data.model.ContactModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class ContactsRepository {

    @OptIn(DelicateCoroutinesApi::class)
    fun update(contactDao: ContactDao, contact: ContactModel) {
        GlobalScope.launch(Dispatchers.IO) {
            contactDao.update(contact)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun delete(contactDao: ContactDao, contact: ContactModel) {
        GlobalScope.launch(Dispatchers.IO) {
            contactDao.delete(contact)
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun getAll(contactDao: ContactDao, onSuccess: (data: MutableList<ContactModel>) -> Unit) {
        val result: Deferred<MutableList<ContactModel>> = GlobalScope.async {
            contactDao.getAll()
        }

        GlobalScope.launch(Dispatchers.Main) {
            onSuccess(result.await())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun insertAll(contactDao: ContactDao, newData: MutableList<ContactModel>) {
        GlobalScope.launch(Dispatchers.IO) {
            contactDao.insertAll(newData)
        }
    }
}