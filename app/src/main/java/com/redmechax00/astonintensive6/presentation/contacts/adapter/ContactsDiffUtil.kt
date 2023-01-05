package com.redmechax00.astonintensive6.presentation.contacts.adapter

import androidx.recyclerview.widget.DiffUtil
import com.redmechax00.astonintensive6.data.model.ContactModel

object ContactsDiffUtil : DiffUtil.ItemCallback<ContactModel>() {

    override fun areItemsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem.contactId == newItem.contactId
    }

    override fun areContentsTheSame(oldItem: ContactModel, newItem: ContactModel): Boolean {
        return oldItem == newItem
    }
}