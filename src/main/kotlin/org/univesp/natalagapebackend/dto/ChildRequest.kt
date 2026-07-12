package org.univesp.natalagapebackend.dto

import org.univesp.natalagapebackend.dto.ChildRequest.Companion.parseBirthDate
import org.univesp.natalagapebackend.models.Child
import org.univesp.natalagapebackend.models.Family
import java.time.LocalDate
import java.time.format.DateTimeFormatter

data class ChildRequest(
    val childId: Long = 0,
    val childName: String,
    val gender: String,
    val birthDate: String,
    val clothes: String? = null,
    val shoes: String? = null,
    val pictureUrl: String? = null,
    val familyId: Long
) {
    companion object {
        private val dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE

        fun parseBirthDate(dateString: String): LocalDate {
            return LocalDate.parse(dateString, dateFormatter)
        }
    }
}

fun ChildRequest.toEntity(family: Family): Child {

    return Child(
        childId = this.childId,
        childName = this.childName,
        birthDate = parseBirthDate(this.birthDate),
        gender = this.gender,
        clothes = this.clothes,
        shoes = this.shoes,
        pictureUrl = this.pictureUrl,
        family = family
    )
}
