package org.univesp.natalagapebackend.dto

import org.univesp.natalagapebackend.models.FoodContribution
import java.time.Year

data class FoodContributionResponse(
    val id : Long,
    val campaignYear : Year,
    val campaignChurch : String,
    val sponsorName : String,
    val responsibleName : String,
    val wasDelivered : Boolean? = null,
)

fun toDTOResponse(foodContribution: FoodContribution): FoodContributionResponse {
    return FoodContributionResponse(
        id = foodContribution.id,
        campaignYear = foodContribution.campaign.campaignYear,
        campaignChurch = foodContribution.campaign.campaignChurch,
        sponsorName = foodContribution.sponsor.sponsorName,
        responsibleName = foodContribution.family.responsibleName,
        wasDelivered = foodContribution.wasDelivered,
    )
}
