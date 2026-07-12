package org.univesp.natalagapebackend.dto

import org.univesp.natalagapebackend.models.ChildContribution

data class ChildContributionEditResponse(
    val id: Long,
    val campaignId: Long,
    val childId: Long,
    val leaderId: Long,
    val sponsorId: Long,
    val wasDelivered: Boolean? = null,
    val acceptance: String,
    val observation: String? = null,
    val isToyDelivered: Boolean? = null
)

fun toDTOEditResponse(childContribution: ChildContribution): ChildContributionEditResponse {
    return ChildContributionEditResponse(
        id = childContribution.id,
        campaignId = childContribution.campaign.campaignId,
        childId = childContribution.child.childId,
        leaderId = childContribution.leadership.leaderId,
        sponsorId = childContribution.sponsor.sponsorId,
        wasDelivered = childContribution.wasDelivered,
        acceptance = childContribution.acceptance.toString(),
        observation = childContribution.observation,
        isToyDelivered = childContribution.toyDelivered ?: false
    )
}
