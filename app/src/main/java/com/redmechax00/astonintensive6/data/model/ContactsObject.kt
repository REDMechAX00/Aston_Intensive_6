package com.redmechax00.astonintensive6.data.model

import android.content.Context
import com.redmechax00.astonintensive6.R
import com.redmechax00.astonintensive6.presentation.extensions.formatToPhone

object ContactsObject {

    fun getContactsViews(context: Context, countOfItems: Int): MutableList<ContactModel> =
        createContactsViews(nameList(context), surnameList(context), countOfItems)

    private fun createContactsViews(
        nameList: List<String>,
        surnameList: List<String>,
        countOfItems: Int
    ): MutableList<ContactModel> {
        val maxUniqueItemCount = nameList.size * surnameList.size
        val count = countOfItems.coerceAtMost(maxUniqueItemCount)

        val uniqueContactsNameSet = HashSet<Pair<String, String>>()
        while (uniqueContactsNameSet.size <= count) {
            val randomName = randomStringFromList(nameList)
            val randomSurname = randomStringFromList(surnameList)
            uniqueContactsNameSet.add(Pair(randomName, randomSurname))
        }

        val contactsViews = mutableListOf<ContactModel>()
        uniqueContactsNameSet.forEach { pair ->
            contactsViews.add(
                ContactModel(
                    getUid(),
                    pair.first,
                    pair.second,
                    randomPhoneNumber()
                )
            )
        }

        return contactsViews
    }

    private fun nameList(context: Context): List<String> = listOf(
        context.getString(R.string.text_name1),
        context.getString(R.string.text_name2),
        context.getString(R.string.text_name3),
        context.getString(R.string.text_name4),
        context.getString(R.string.text_name5),
        context.getString(R.string.text_name6),
        context.getString(R.string.text_name7),
        context.getString(R.string.text_name8),
        context.getString(R.string.text_name9),
        context.getString(R.string.text_name10),
        context.getString(R.string.text_name11),
        context.getString(R.string.text_name12),
        context.getString(R.string.text_name13),
        context.getString(R.string.text_name14),
        context.getString(R.string.text_name15),
        context.getString(R.string.text_name16),
        context.getString(R.string.text_name17),
        context.getString(R.string.text_name18),
        context.getString(R.string.text_name19),
        context.getString(R.string.text_name20),
        context.getString(R.string.text_name21),
        context.getString(R.string.text_name22),
        context.getString(R.string.text_name23),
        context.getString(R.string.text_name24),
        context.getString(R.string.text_name25),
        context.getString(R.string.text_name26),
        context.getString(R.string.text_name27),
        context.getString(R.string.text_name28),
        context.getString(R.string.text_name29),
        context.getString(R.string.text_name30),
        context.getString(R.string.text_name31),
        context.getString(R.string.text_name32),
        context.getString(R.string.text_name33),
        context.getString(R.string.text_name34),
        context.getString(R.string.text_name35),
        context.getString(R.string.text_name36),
        context.getString(R.string.text_name37),
        context.getString(R.string.text_name38),
        context.getString(R.string.text_name39),
        context.getString(R.string.text_name40),
        context.getString(R.string.text_name41),
        context.getString(R.string.text_name42),
        context.getString(R.string.text_name43),
        context.getString(R.string.text_name44),
        context.getString(R.string.text_name45),
        context.getString(R.string.text_name46),
        context.getString(R.string.text_name47),
        context.getString(R.string.text_name48),
        context.getString(R.string.text_name49),
        context.getString(R.string.text_name50),
        context.getString(R.string.text_name51),
        context.getString(R.string.text_name52),
        context.getString(R.string.text_name53),
        context.getString(R.string.text_name54),
        context.getString(R.string.text_name55),
        context.getString(R.string.text_name56),
        context.getString(R.string.text_name57),
        context.getString(R.string.text_name58),
        context.getString(R.string.text_name59),
        context.getString(R.string.text_name60),
        context.getString(R.string.text_name61),
        context.getString(R.string.text_name62),
        context.getString(R.string.text_name63),
        context.getString(R.string.text_name64),
        context.getString(R.string.text_name65),
        context.getString(R.string.text_name66),
        context.getString(R.string.text_name67),
        context.getString(R.string.text_name68),
        context.getString(R.string.text_name69),
        context.getString(R.string.text_name70),
        context.getString(R.string.text_name71),
        context.getString(R.string.text_name72),
        context.getString(R.string.text_name73),
        context.getString(R.string.text_name74)
    )

    private fun surnameList(context: Context): List<String> = listOf(
        context.getString(R.string.text_surname1),
        context.getString(R.string.text_surname2),
        context.getString(R.string.text_surname3),
        context.getString(R.string.text_surname4),
        context.getString(R.string.text_surname5),
        context.getString(R.string.text_surname6),
        context.getString(R.string.text_surname7),
        context.getString(R.string.text_surname8),
        context.getString(R.string.text_surname9),
        context.getString(R.string.text_surname10),
        context.getString(R.string.text_surname11),
        context.getString(R.string.text_surname12),
        context.getString(R.string.text_surname13),
        context.getString(R.string.text_surname14),
        context.getString(R.string.text_surname15),
        context.getString(R.string.text_surname16),
        context.getString(R.string.text_surname17),
        context.getString(R.string.text_surname18),
        context.getString(R.string.text_surname19),
        context.getString(R.string.text_surname20),
        context.getString(R.string.text_surname21),
        context.getString(R.string.text_surname22),
        context.getString(R.string.text_surname23),
        context.getString(R.string.text_surname24),
        context.getString(R.string.text_surname25),
        context.getString(R.string.text_surname26),
        context.getString(R.string.text_surname27),
        context.getString(R.string.text_surname28),
        context.getString(R.string.text_surname29),
        context.getString(R.string.text_surname30),
        context.getString(R.string.text_surname31),
        context.getString(R.string.text_surname32),
        context.getString(R.string.text_surname33),
        context.getString(R.string.text_surname34),
        context.getString(R.string.text_surname35),
        context.getString(R.string.text_surname36),
        context.getString(R.string.text_surname37),
        context.getString(R.string.text_surname38),
        context.getString(R.string.text_surname39),
        context.getString(R.string.text_surname40),
        context.getString(R.string.text_surname41),
        context.getString(R.string.text_surname42),
        context.getString(R.string.text_surname43),
        context.getString(R.string.text_surname44),
        context.getString(R.string.text_surname45),
        context.getString(R.string.text_surname46),
        context.getString(R.string.text_surname47),
        context.getString(R.string.text_surname48),
        context.getString(R.string.text_surname49),
        context.getString(R.string.text_surname50),
        context.getString(R.string.text_surname51),
        context.getString(R.string.text_surname52),
        context.getString(R.string.text_surname53),
        context.getString(R.string.text_surname54),
        context.getString(R.string.text_surname55),
        context.getString(R.string.text_surname56),
        context.getString(R.string.text_surname57),
        context.getString(R.string.text_surname58),
        context.getString(R.string.text_surname59),
        context.getString(R.string.text_surname60),
        context.getString(R.string.text_surname61),
        context.getString(R.string.text_surname62),
        context.getString(R.string.text_surname63),
        context.getString(R.string.text_surname64),
        context.getString(R.string.text_surname65),
        context.getString(R.string.text_surname66),
        context.getString(R.string.text_surname67),
        context.getString(R.string.text_surname68),
        context.getString(R.string.text_surname69),
        context.getString(R.string.text_surname70),
        context.getString(R.string.text_surname71),
        context.getString(R.string.text_surname72),
        context.getString(R.string.text_surname73),
        context.getString(R.string.text_surname74)
    )

    private var countId: Int = 0
    private fun getUid(): Int = countId++

    private fun randomStringFromList(list: List<String>): String {
        val rnd = (list.indices).random()
        return list[rnd]
    }

    private fun randomPhoneNumber(): String {
        var phone = "+1"
        for (i in 0 until 10) {
            val rnd = (0..9).random()
            phone += rnd.toString()
        }
        return phone.formatToPhone()
    }
}