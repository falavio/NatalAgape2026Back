package org.univesp.natalagapebackend.dto

import org.univesp.natalagapebackend.models.Child
import org.univesp.natalagapebackend.models.Family

data class FamilyWithChildrenDTO(
    val familyId: Long,
    val responsibleName: String,
    val phoneNumber: String,
    val address: String,
    val neighborhoodId: Long,
    val neighborhoodName: String,
    val observation: String? = null,
    val children: List<Children>? = null,
    val leaderId: Long,
    val leaderName: String,
    val pictureUrl: String?,
    val pictureSubscription: String? = null,
    val campaignId: Long? = null,
    val campaignName: String? = null
)

data class Children(
    val childId: Long,
    val childName : String,
    val gender: String,
    val age: Int,
    val clothes: String?,
    val shoes: String?,
    val pictureUrl: String?
    )

fun toDTOOutput(family: Family, children: List<Child>? ): FamilyWithChildrenDTO {
    return FamilyWithChildrenDTO(
        familyId = family.familyId,
        responsibleName = family.responsibleName,
        phoneNumber = family.phoneNumber,
        address = family.address,
        neighborhoodId = family.neighborhood.neighborhoodId,
        neighborhoodName = family.neighborhood.neighborhoodName,
        observation = family.observation,
        children = children?.map {
            Children(
                childId = it.childId,
                childName = it.childName,
                gender = it.gender,
                age = it.age,
                clothes = it.clothes,
                shoes = it.shoes,
                pictureUrl = it.pictureUrl

            )
        },
        leaderId = family.leadership.leaderId,
        leaderName = family.leadership.leaderName,
        pictureUrl = family.pictureUrl,
        pictureSubscription = family.pictureSubscription,
        campaignId = family.campaign.campaignId,
        campaignName = "${family.campaign.campaignYear} - ${family.campaign.campaignChurch}"
    )
}