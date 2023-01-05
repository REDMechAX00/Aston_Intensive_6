package com.redmechax00.astonintensive6.presentation.contacts

import com.redmechax00.astonintensive6.data.model.ContactModel
import com.redmechax00.astonintensive6.presentation.contacts.adapter.ContactsAdapter

interface ItemLongClickListener {
    fun onItemLongClicked(holder: ContactsAdapter.ContactsHolder, contact: ContactModel)
}