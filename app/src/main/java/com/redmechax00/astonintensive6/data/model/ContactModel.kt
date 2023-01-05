package com.redmechax00.astonintensive6.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ContactModel(
    @PrimaryKey @ColumnInfo(name = "contact_id") val contactId: Int = -1,
    @ColumnInfo(name = "contact_name") var contactName: String = "",
    @ColumnInfo(name = "contact_surname") var contactSurname: String = "",
    @ColumnInfo(name = "contact_phone") var contactPhone: String = "",
    @ColumnInfo(name = "contact_photo_url") var contactPhotoUrl: String? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ContactModel
        if (contactId != other.contactId) return false
        if (contactName != other.contactName) return false
        if (contactSurname != other.contactSurname) return false
        if (contactPhone != other.contactPhone) return false
        if (contactPhotoUrl != other.contactPhotoUrl) return false

        return true
    }

    fun getFullName(): String {
        return "$contactName $contactSurname"
    }

    override fun hashCode(): Int {
        var result = contactId
        result = 31 * result + contactName.hashCode()
        result = 31 * result + contactSurname.hashCode()
        result = 31 * result + contactPhone.hashCode()
        result = 31 * result + (contactPhotoUrl?.hashCode() ?: 0)
        return result
    }
}