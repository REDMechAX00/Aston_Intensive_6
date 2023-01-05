package com.redmechax00.astonintensive6.presentation.contacts

import com.redmechax00.astonintensive6.data.model.ContactModel

interface ItemClickListener {
    fun onItemClicked(contact: ContactModel)
}