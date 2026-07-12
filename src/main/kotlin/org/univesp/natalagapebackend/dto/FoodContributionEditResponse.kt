package org.univesp.natalagapebackend.dto

import org.univesp.natalagapebackend.models.FoodContribution

data class FoodContributionEditResponse(
    val id: Long,
    val campaignId: Long,
    val familyId: Long,
    val leaderId: Long,
    val sponsorId: Long,
    val wasDelivered: Boolean? = null,
    val paidInSpecies: Boolean? = null,
    val donationDate: String,
    val observation: String? = null,
)

fun toDTOEditResponse(foodContribution: FoodContribution): FoodContributionEditResponse {
    return FoodContributionEditResponse(
        id = foodContribution.id,
        campaignId = foodContribution.campaign.campaignId,
        familyId = foodContribution.family.familyId,
        leaderId = foodContribution.leader.leaderId,
        sponsorId = foodContribution.sponsor.sponsorId,
        wasDelivered = foodContribution.wasDelivered,
        paidInSpecies = foodContribution.paidInSpecies,
        donationDate = foodContribution.donationDate.toString(),
        observation = foodContribution.observation
    )
}
