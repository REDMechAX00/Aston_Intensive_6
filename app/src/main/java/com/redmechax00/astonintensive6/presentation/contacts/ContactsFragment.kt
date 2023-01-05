package com.redmechax00.astonintensive6.presentation.contacts

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.size
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.room.Room
import com.redmechax00.astonintensive6.R
import com.redmechax00.astonintensive6.data.local.ContactDatabase.Companion.CONTACTS_DATABASE
import com.redmechax00.astonintensive6.data.local.ContactDao
import com.redmechax00.astonintensive6.data.local.ContactDatabase
import com.redmechax00.astonintensive6.data.model.ContactModel
import com.redmechax00.astonintensive6.data.model.ContactsObject
import com.redmechax00.astonintensive6.databinding.FragmentContactsBinding
import com.redmechax00.astonintensive6.presentation.extensions.addAfterTextChangedListener
import com.redmechax00.astonintensive6.presentation.extensions.clearText
import com.redmechax00.astonintensive6.presentation.extensions.gone
import com.redmechax00.astonintensive6.presentation.extensions.show
import com.redmechax00.astonintensive6.presentation.extensions.showKeyboard
import com.redmechax00.astonintensive6.presentation.extensions.hideKeyboard
import com.redmechax00.astonintensive6.presentation.extensions.hide
import com.redmechax00.astonintensive6.presentation.extensions.isTablet
import com.redmechax00.astonintensive6.presentation.extensions.backStackIsNotEmpty
import com.redmechax00.astonintensive6.presentation.BaseFragment
import com.redmechax00.astonintensive6.presentation.contact_details.ContactDetailsFragment
import com.redmechax00.astonintensive6.presentation.contacts.adapter.ContactsAdapter
import com.redmechax00.astonintensive6.presentation.contacts.adapter.ContactsItemDecoration
import com.redmechax00.astonintensive6.presentation.viewmodel.ContactsViewModel
import com.redmechax00.astonintensive6.presentation.viewmodel.ContactsViewModelFactory
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay

class ContactsFragment : BaseFragment<FragmentContactsBinding>(R.layout.fragment_contacts),
    ItemClickListener, ItemLongClickListener {

    private lateinit var toolbarTitle: TextView
    private lateinit var toolbarBtnSearch: ImageButton
    private lateinit var toolbarBtnCancel: ImageButton
    private lateinit var toolbarBtnClear: ImageButton
    private lateinit var toolbarEditTextSearch: EditText

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ContactsAdapter

    private lateinit var contactModelList: ContactsViewModel

    override fun createBinding(view: View): FragmentContactsBinding =
        FragmentContactsBinding.bind(view)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFields()
        initData()
        setOnClickListeners()
        addTextChangedListener()
    }

    override fun onResume() {
        super.onResume()
        updateSearchingVisibility(toolbarEditTextSearch.text.toString())
    }

    private fun initFields() {
        val toolbarBinding = binding.toolbarContactsContainer
        val toolbar = toolbarBinding.toolbarContacts
        toolbarTitle = toolbarBinding.contactsToolbarTitle
        toolbarBtnSearch = toolbarBinding.contactsToolbarBtnSearch
        toolbarBtnCancel = toolbarBinding.contactsToolbarBtnCancel
        toolbarBtnClear = toolbarBinding.contactsToolbarBtnClear
        toolbarEditTextSearch = toolbarBinding.contactsToolbarEditTextSearch

        val activity = requireActivity()
        if (activity is AppCompatActivity) {
            activity.setSupportActionBar(toolbar)
            activity.supportActionBar?.setDisplayShowTitleEnabled(false)
        }

        recyclerView = binding.contactsRecyclerView
        adapter = ContactsAdapter(this, this)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        (recyclerView.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        recyclerView.adapter = adapter
    }

    private fun initData() {
        val initDataList = createInitDataList(250)
        val contactDao = initDatabase()

        contactModelList = ViewModelProvider(
            requireActivity(),
            ContactsViewModelFactory(initDataList, contactDao)
        )[ContactsViewModel::class.java]

        contactModelList.contactsLive.observe(viewLifecycleOwner) { data ->
            updateAdapterWithFilter(data)
        }

        contactModelList.editedItemLive.observe(viewLifecycleOwner) { contact ->
            contactModelList.updateItem(contact)
        }

        recyclerView.addItemDecoration(ContactsItemDecoration(requireContext()))
    }

    @Suppress("SameParameterValue")
    private fun createInitDataList(countOfItem: Int) =
        ContactsObject.getContactsViews(requireActivity(), countOfItem)

    private fun initDatabase(): ContactDao {
        val db: ContactDatabase = Room.databaseBuilder(
            requireActivity(),
            ContactDatabase::class.java, CONTACTS_DATABASE
        ).build()
        return db.getContactDao()
    }

    private fun addTextChangedListener() {
        toolbarEditTextSearch.addAfterTextChangedListener { text ->
            updateBtnClearVisibility(text)
            updateAdapterWithFilter(null)
        }
    }

    private fun updateAdapterWithFilter(newData: MutableList<ContactModel>?) {
        val textFilter = toolbarEditTextSearch.text.toString()
        var displayedData =
            newData ?: contactModelList.contactsLive.value ?: mutableListOf()

        //Filter
        if (textFilter.isNotEmpty()) {
            val tmp: MutableList<ContactModel> = mutableListOf()
            for (item in displayedData) {
                val itemName = item.getFullName().lowercase()
                if (itemName.contains(textFilter.lowercase())) {
                    tmp.add(item)
                }
            }
            displayedData = tmp
        }

        adapter.updateData(displayedData)

        if (newData.isNullOrEmpty()) {
            recyclerView.smartScrollToTop()
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun RecyclerView.smartScrollToTop() {
        if (isSearchingActivated()) {
            GlobalScope.launch(Dispatchers.Main) {
                delay(200)
                if (this@smartScrollToTop.size > 1) {
                    this@smartScrollToTop.layoutManager?.scrollToPosition(0)
                }
            }
        }
    }

    private fun isSearchingActivated() = toolbarEditTextSearch.visibility == View.VISIBLE

    private fun setOnClickListeners() {
        toolbarBtnSearch.setOnClickListener {
            startSearching()
        }

        toolbarBtnCancel.setOnClickListener {
            cancelSearching()
        }

        toolbarBtnClear.setOnClickListener {
            toolbarEditTextSearch.clearText()
        }
    }

    override fun onItemClicked(contact: ContactModel) {
        startInfoFragment(contact)
    }

    override fun onItemLongClicked(holder: ContactsAdapter.ContactsHolder, contact: ContactModel) {
        showContactPopupMenu(holder, contact)
    }

    private fun showContactPopupMenu(
        holder: ContactsAdapter.ContactsHolder,
        contact: ContactModel
    ) {
        val wrapper: Context = ContextThemeWrapper(requireContext(), R.style.Contacts_PopupMenu)
        val popup = PopupMenu(wrapper, holder.itemView)
        popup.inflate(R.menu.popup_menu_contact_item)

        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.popup_menu_item_delete -> {
                    deleteContact(contact)
                }
            }
            true
        }

        popup.show()
    }

    private fun deleteContact(contact: ContactModel) {
        contactModelList.deleteItem(contact)
    }

    private fun startSearching() {
        toolbarTitle.gone()
        toolbarBtnSearch.gone()
        updateBtnClearVisibility(toolbarEditTextSearch.text.toString())
        toolbarBtnCancel.show()
        toolbarEditTextSearch.show()
        toolbarEditTextSearch.requestFocus()
        toolbarEditTextSearch.showKeyboard(requireContext())
    }

    private fun cancelSearching() {
        toolbarBtnCancel.gone()
        toolbarBtnClear.gone()
        toolbarEditTextSearch.gone()
        toolbarEditTextSearch.clearText()
        toolbarEditTextSearch.hideKeyboard(requireContext())
        toolbarTitle.show()
        toolbarBtnSearch.show()
    }

    private fun startInfoFragment(contact: ContactModel) {
        contactModelList.setItemToEditedContainer(contact)

        if (requireActivity().backStackIsNotEmpty()) {
            requireActivity().supportFragmentManager.popBackStack()
        }

        val infoFrag = ContactDetailsFragment()
        val transaction = requireActivity().supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            R.anim.enter,
            R.anim.exit,
            R.anim.pop_enter,
            R.anim.pop_exit
        )
        transaction.addToBackStack(null)

        if (isTablet(requireActivity())) {
            transaction.replace(R.id.main_second_fragment_container, infoFrag)
        } else {
            transaction.replace(R.id.main_first_fragment_container, infoFrag)
        }

        transaction.commit()
    }

    private fun updateSearchingVisibility(text: String) {
        if (text.isNotEmpty()) {
            startSearching()
        }
    }

    private fun updateBtnClearVisibility(text: String) {
        if (text.isEmpty()) {
            toolbarBtnClear.hide()
        } else {
            toolbarBtnClear.show()
        }
    }
}