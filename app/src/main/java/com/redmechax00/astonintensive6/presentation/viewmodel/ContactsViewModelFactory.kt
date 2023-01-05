package com.redmechax00.astonintensive6.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.redmechax00.astonintensive6.data.local.ContactDao
import com.redmechax00.astonintensive6.data.model.ContactModel

class ContactsViewModelFactory(
    private val initData: MutableList<ContactModel>,
    private val contactDao: ContactDao
) :
    ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        ContactsViewModel(initData, contactDao) as T
}