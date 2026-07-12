package org.univesp.natalagapebackend.dto

import org.univesp.natalagapebackend.models.Family
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.models.Neighborhood

data class FamilyDTOInput(
    val familyId: Long? = null,
    val responsibleName: String,
    val phoneNumber: String,
    val address: String,
    val neighborhoodId: Long,
    val observation: String? = null,
    val leaderId: Long,
    val pictureUrl: String? = null,
    val pictureSubscription : String? = null
)

fun FamilyDTOInput.toEntity(neighborhood: Neighborhood, leadership: Leadership): Family {
    return Family(
        familyId = this.familyId ?: 0,
        responsibleName = this.responsibleName,
        phoneNumber = this.phoneNumber,
        address = this.address,
        neighborhood = neighborhood,
        observation = this.observation,
        leadership = leadership,
        pictureUrl = this.pictureUrl,
        pictureSubscription = this.pictureSubscription
    )
}