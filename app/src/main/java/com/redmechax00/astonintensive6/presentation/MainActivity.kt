package com.redmechax00.astonintensive6.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.redmechax00.astonintensive6.R
import com.redmechax00.astonintensive6.presentation.contacts.ContactsFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            startContactsFragment()
        }
    }

    private fun startContactsFragment() {
        this.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
            .replace(R.id.main_first_fragment_container, ContactsFragment())
            .commit()
    }
}