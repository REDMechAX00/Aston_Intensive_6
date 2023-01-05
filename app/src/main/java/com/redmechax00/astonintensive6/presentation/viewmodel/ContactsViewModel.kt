package com.redmechax00.astonintensive6.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.redmechax00.astonintensive6.data.ContactsRepository
import com.redmechax00.astonintensive6.data.local.ContactDao
import com.redmechax00.astonintensive6.data.model.ContactModel
import java.io.InputStream
import java.net.URL
import java.net.URLConnection
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class ContactsViewModel(initData: MutableList<ContactModel>, private val contactDao: ContactDao) :
    ViewModel() {

    private val repository = ContactsRepository()

    private val contactsLiveMutable: MutableLiveData<MutableList<ContactModel>> = MutableLiveData()
    val contactsLive: LiveData<MutableList<ContactModel>> = contactsLiveMutable

    private val editedItemLiveMutable: MutableLiveData<ContactModel> = MutableLiveData()
    val editedItemLive: LiveData<ContactModel> = editedItemLiveMutable

    init {
        repository.getAll(contactDao) { data ->
            if (data.isEmpty()) {
                updateLiveData(initData)
                scanAndFixNullPhotoInBackground(initData)
            } else {
                contactsLiveMutable.value = data
                scanAndFixNullPhotoInBackground(data)
            }
        }
    }

    fun deleteItem(deletedItem: ContactModel) {
        val data = contactsLiveMutable.value
        if (data != null) {
            val item = data.find { it.contactId == deletedItem.contactId }
            data.remove(item)
        }

        repository.delete(contactDao, deletedItem)
        contactsLiveMutable.value = data
    }

    fun updateItem(newItem: ContactModel) {
        val data = contactsLiveMutable.value
        if (data != null) {
            val item = data.find { it.contactId == newItem.contactId }
            item?.let {
                item.contactName = newItem.contactName
                item.contactSurname = newItem.contactSurname
                item.contactPhone = newItem.contactPhone
                item.contactPhotoUrl = newItem.contactPhotoUrl

                repository.update(contactDao, item)
                contactsLiveMutable.value = data
        }
        }
    }

    private fun updateLiveData(newData: MutableList<ContactModel>) {
        contactsLiveMutable.value = newData
        repository.insertAll(contactDao, newData)
    }

    fun setItemToEditedContainer(item: ContactModel) {
        editedItemLiveMutable.value = item
    }

    fun getLastId(): Int {
        val listOfId = mutableListOf<Int>()
        contactsLiveMutable.value?.forEach {
            listOfId.add(it.contactId)
        }
        listOfId.sortBy { it.inv() }

        return if (listOfId.isNotEmpty()) listOfId[0]
        else 0
    }

    private fun scanAndFixNullPhotoInBackground(data: MutableList<ContactModel>) {
        val targetPhotoUrlsForChange = getItemsIdWithNullPhotoUrl(data)

        targetPhotoUrlsForChange.loadPhotoUrls { photoIdAndUrl ->
            val contact = getItemById(photoIdAndUrl.first)
            val photoUrl = photoIdAndUrl.second
            applyPhotoUrl(contact, photoUrl)
        }
    }

    private fun getItemById(contactId: Int): ContactModel? =
        contactsLiveMutable.value?.find { it.contactId == contactId }

    private fun getItemsIdWithNullPhotoUrl(data: MutableList<ContactModel>): HashMap<Int, String?> {
        val nullPhotoUrlItems = hashMapOf<Int, String?>()
        data.forEach { item ->
            val itemId = item.contactId
            if (item.contactPhotoUrl == null) {
                nullPhotoUrlItems[itemId] = null
            }
        }
        return nullPhotoUrlItems
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun HashMap<Int, String?>.loadPhotoUrls(
        onSuccess: (Pair<Int, String>) -> Unit
    ) {
        with(GlobalScope) {
            val deferredTasks: MutableList<Deferred<Pair<Int, String>>> = mutableListOf()

            for (item in this@loadPhotoUrls) {
                deferredTasks.add(async { getPhotoSourceUrl(item.key) })
            }

            //Async Load
            GlobalScope.launch(Dispatchers.Main) {
                for (i in deferredTasks.indices) {
                    val photoValue =
                        Pair(deferredTasks[i].await().first, deferredTasks[i].await().second)
                    onSuccess(photoValue)
                }
            }
        }
    }

    private fun getPhotoSourceUrl(itemId: Int): Pair<Int, String> {
        val con: URLConnection = URL("https://picsum.photos/350/350").openConnection()
        con.connect()
        val input: InputStream = con.getInputStream()
        input.close()
        return Pair(itemId, con.url.toString())
    }

    private fun applyPhotoUrl(
        item: ContactModel?,
        photoUrl: String
    ) {
        item?.let {
            item.contactPhotoUrl = photoUrl
            updateItem(item)
        }
    }
}