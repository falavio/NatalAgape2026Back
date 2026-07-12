package org.univesp.natalagapebackend.models.DTO


import org.univesp.natalagapebackend.models.Family
data class FamilyDTOOutput(
    val familyId: Long,
    val responsibleName: String,
    val phoneNumber: String,
    val address: String,
    val neighborhoodId: Long? = null,
    val neighborhoodName: String? = null,
    val observation: String? = null,
    val leaderId: Long? = null,
    val leaderName: String,
    val pictureUrl: String? = null,
    val pictureSubscription: String? = null
)

fun Family.toDTOOutput(): FamilyDTOOutput {
    return FamilyDTOOutput(
        familyId = this.familyId,
        responsibleName = this.responsibleName,
        phoneNumber = this.phoneNumber,
        address = this.address,
        neighborhoodName = this.neighborhood.neighborhoodName,
        observation = this.observation,
        leaderId = this.leadership.leaderId,
        leaderName = this.leadership.leaderName,
        pictureUrl = this.pictureUrl,
        pictureSubscription = this.pictureSubscription
    )
}

fun Family.toDTOOutputWithNeighborhoodId(): FamilyDTOOutput {
    return FamilyDTOOutput(
        familyId = this.familyId,
        responsibleName = this.responsibleName,
        phoneNumber = this.phoneNumber,
        address = this.address,
        neighborhoodId = this.neighborhood.neighborhoodId,
        observation = this.observation,
        leaderId = this.leadership.leaderId,
        leaderName = this.leadership.leaderName
    )
}
