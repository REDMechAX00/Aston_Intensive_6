package com.redmechax00.astonintensive6.presentation.contact_details

import android.os.Bundle
import android.telephony.PhoneNumberFormattingTextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import coil.load
import com.redmechax00.astonintensive6.R
import com.redmechax00.astonintensive6.data.model.ContactModel
import com.redmechax00.astonintensive6.presentation.viewmodel.ContactsViewModel
import com.redmechax00.astonintensive6.databinding.FragmentContactDetailsBinding
import com.redmechax00.astonintensive6.presentation.extensions.hideKeyboard
import com.redmechax00.astonintensive6.presentation.extensions.hideTabletFragmentContainer
import com.redmechax00.astonintensive6.presentation.extensions.isTablet
import com.redmechax00.astonintensive6.presentation.extensions.showTabletFragmentContainer
import com.redmechax00.astonintensive6.presentation.BaseFragment

class ContactDetailsFragment : BaseFragment<FragmentContactDetailsBinding>(R.layout.fragment_contact_details) {

    private lateinit var toolbarBtnBack: ImageButton
    private lateinit var toolbarBtnDone: ImageButton

    private lateinit var contactName: EditText
    private lateinit var contactSurname: EditText
    private lateinit var contactPhone: EditText
    private lateinit var contactPhoto: ImageView

    private lateinit var contactModelList: ContactsViewModel

    private var editedItem: ContactModel? = null

    override fun createBinding(view: View): FragmentContactDetailsBinding = FragmentContactDetailsBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
        initData()
        addPhoneNumberFormattingTextWatcher()
        setOnClickListeners()
    }

    private fun initFields() {
        val toolbarBinding = binding.toolbarContactDetailsContainer
        val toolbar = toolbarBinding.toolbarContactDetails
        toolbarBtnBack = toolbarBinding.contactDetailsToolbarBtnBack
        toolbarBtnDone = toolbarBinding.contactDetailsToolbarBtnDone

        val requireActivity = requireActivity()
        if (requireActivity is AppCompatActivity) {
            requireActivity.setSupportActionBar(toolbar)
            requireActivity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        contactName = binding.contactDetailsName
        contactSurname = binding.contactDetailsSurname
        contactPhone = binding.contactDetailsPhone
        contactPhoto = binding.contactDetailsPhoto
    }

    private fun initData() {
        contactModelList = ViewModelProvider(requireActivity())[ContactsViewModel::class.java]

        editedItem = contactModelList.editedItemLive.value
        editedItem?.let { editedItem ->
            contactName.setText(editedItem.contactName)
            contactSurname.setText(editedItem.contactSurname)
            contactPhone.setText(editedItem.contactPhone)

            if (editedItem.contactPhotoUrl == null) {
                contactPhoto.setImageResource(R.drawable.default_photo)
            } else {
                contactPhoto.load(editedItem.contactPhotoUrl)
            }
        }
    }

    private fun addPhoneNumberFormattingTextWatcher() {
        contactPhone.addTextChangedListener(PhoneNumberFormattingTextWatcher())
    }

    private fun setOnClickListeners() {
        toolbarBtnBack.setOnClickListener {
            closeFragment()
        }

        toolbarBtnDone.setOnClickListener {
            saveChanges()
            closeFragment()
        }
    }

    private fun saveChanges() {
        val contact = ContactModel(
            editedItem?.contactId ?: contactModelList.getLastId(),
            contactName.text.toString(),
            contactSurname.text.toString(),
            contactPhone.text.toString(),
            editedItem?.contactPhotoUrl
        )
        contactModelList.setItemToEditedContainer(contact)
    }

    private fun closeFragment() {
        view?.hideKeyboard(requireContext())
        requireActivity().supportFragmentManager.popBackStack()
    }

    override fun onStart() {
        super.onStart()
        if (isTablet(requireActivity())) {
            requireActivity().showTabletFragmentContainer()
        }
    }

    override fun onStop() {
        super.onStop()
        if (isTablet(requireActivity())) {
            requireActivity().hideTabletFragmentContainer()
        }
    }
}