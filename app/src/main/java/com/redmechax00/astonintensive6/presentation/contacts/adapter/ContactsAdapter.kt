package com.redmechax00.astonintensive6.presentation.contacts.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.redmechax00.astonintensive6.R
import com.redmechax00.astonintensive6.databinding.ContactItemBinding
import com.redmechax00.astonintensive6.presentation.extensions.deepCopy
import com.redmechax00.astonintensive6.data.model.ContactModel
import com.redmechax00.astonintensive6.presentation.contacts.ItemClickListener
import com.redmechax00.astonintensive6.presentation.contacts.ItemLongClickListener
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

class ContactsAdapter(
    private val itemClickListener: ItemClickListener,
    private val itemLongClickListener: ItemLongClickListener
) :
    ListAdapter<ContactModel, ContactsAdapter.ContactsHolder>(
        ContactsDiffUtil
    ) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ContactItemBinding.inflate(inflater, parent, false)
        return ContactsHolder(binding)
    }

    override fun onBindViewHolder(holder: ContactsHolder, position: Int) {
        holder.bind(currentList[position])
    }

    override fun onViewAttachedToWindow(holder: ContactsHolder) {
        holder.onAttach(itemClickListener, itemLongClickListener)
    }

    override fun onViewDetachedFromWindow(holder: ContactsHolder) {
        holder.onDetach()
    }

    override fun getItemCount() = currentList.size

    fun updateData(newData: MutableList<ContactModel>) {
        newData.sortBy { it.contactName }
        submitList(newData.deepCopy())
    }

    class ContactsHolder(
        binding: ContactItemBinding
    ) :
        RecyclerView.ViewHolder(binding.root) {

        private lateinit var contact: ContactModel

        private val contactFullName = binding.itemContactFullName
        private val contactPhone = binding.itemContactPhone
        private val contactPhoto = binding.itemContactPhoto

        fun bind(model: ContactModel) {
            contact = model
            contactFullName.text = contact.getFullName()
            contactPhone.text = contact.contactPhone

            val itemPhotoUrl = contact.contactPhotoUrl?.toHttpUrlOrNull()
            if (itemPhotoUrl != null) {
                contactPhoto.load(itemPhotoUrl)
            } else {
                contactPhoto.setImageResource(R.drawable.default_photo)
            }
        }

        fun onAttach(
            itemClickListener: ItemClickListener,
            itemLongClickListener: ItemLongClickListener
        ) {
            itemView.setOnClickListener {
                itemClickListener.onItemClicked(contact)
            }
            itemView.setOnLongClickListener {
                itemLongClickListener.onItemLongClicked(this, contact)
                true
            }
        }

        fun onDetach() {
            itemView.setOnClickListener(null)
            itemView.setOnClickListener(null)
        }
    }
}