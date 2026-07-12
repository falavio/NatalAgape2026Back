package org.univesp.natalagapebackend.dto

import org.univesp.natalagapebackend.models.Child
import org.univesp.natalagapebackend.models.Family
import java.time.LocalDate

data class ChildResponse(
    val childId: Long? = null,
    val childName: String,
    val gender: String,
    val birthDate: LocalDate,
    val clothes: String? = null,
    val shoes: String? = null,
    val pictureUrl: String? = null,
    val familyId: Long
)
fun Child.toResponse(family: Family): ChildResponse {

    return ChildResponse(
        childId = this.childId,
        childName = this.childName,
        birthDate = this.birthDate,
        gender   = this.gender,
        clothes = this.clothes,
        shoes = this.shoes,
        pictureUrl = this.pictureUrl,
        familyId = family.familyId

    )
}
