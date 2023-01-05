package com.redmechax00.astonintensive6.presentation.extensions

import android.content.Context
import android.content.res.Configuration
import android.telephony.PhoneNumberUtils
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.redmechax00.astonintensive6.R
import com.redmechax00.astonintensive6.data.model.ContactModel
import java.util.Locale

fun View.hideKeyboard(context: Context) {
    val inputMethodManager =
        context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(this.windowToken, 0)
}

fun View.showKeyboard(context: Context) {
    val inputMethodManager =
        context.getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

fun isTablet(context: Context): Boolean =
    context.resources.configuration.screenLayout and
            Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_LARGE

fun FragmentActivity.backStackIsNotEmpty(): Boolean =
    this.supportFragmentManager.backStackEntryCount != 0

fun FragmentActivity.showTabletFragmentContainer() {
    val mainSecondContainer =
        this.findViewById<View>(R.id.main_second_fragment_container)
    mainSecondContainer.visibility = View.VISIBLE
}

fun FragmentActivity.hideTabletFragmentContainer() {
    val mainSecondContainer =
        this.findViewById<View>(R.id.main_second_fragment_container)
    mainSecondContainer.visibility = View.GONE
}

fun EditText.addAfterTextChangedListener(onSuccess: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
        override fun afterTextChanged(editable: Editable) {
            onSuccess.invoke(editable.toString())
        }
    })
}

fun MutableList<ContactModel>.deepCopy() = this.map { it.copy() }

fun String.formatToPhone(): String =
    PhoneNumberUtils.formatNumber(this, Locale.getDefault().country)

fun EditText.clearText() {
    setText("")
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.INVISIBLE
}

fun View.gone() {
    visibility = View.GONE
}