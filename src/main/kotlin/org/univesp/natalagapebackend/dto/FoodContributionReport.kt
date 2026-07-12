package org.univesp.natalagapebackend.dto

import org.univesp.natalagapebackend.models.Color
import org.univesp.natalagapebackend.models.Family
import org.univesp.natalagapebackend.models.FoodContribution
import java.math.BigDecimal

data class FoodContributionReport(
    val familiesWithContribution: Int,
    val familiesWithNoContribution: Int,
    val familiesWithPendingContribution: Int,
    val totalActiveFamilies: Int,
    val valueNoContribution: BigDecimal,
    val valuePendingContribution: BigDecimal,
    val familiesWithContributionList: List<FamiliesWithContribution>,
    val familiesWithNoContributionList: List<FamiliesWithNoContribution>,
    val familiesWithPendingContributionList: List<FamiliesWithPendingContribution>,
)

data class FamiliesWithContribution(
    val responsibleName: String,
    val leaderName: String,
    val leaderColor: Color,
)

data class FamiliesWithNoContribution(
    val responsibleName: String,
    val neighborhoodName: String,
    val leaderName: String,
    val totalChildren: Int,
)

data class FamiliesWithPendingContribution(
    val responsibleName: String,
    val totalChildren: Int,
    val sponsorName: String,
    val sponsorPhone: String,
    val paidInSpecies: Boolean?,
    val leaderName: String
)

fun toDTOReport(
    foodContributions: List<FoodContribution>,
    families: List<Family>,
): FoodContributionReport {
    val valueDonation = foodContributions.firstOrNull()?.campaign?.valueDonation ?: 0.toBigDecimal()
    val familiesWithPendingContribution = foodContributions.filter { it.wasDelivered == false && it.donationDate == null }
    val familiesWithContribution = foodContributions.filter { it.wasDelivered == true && it.donationDate != null }
    val familiesWithNoContribution = families.filter { family ->
        foodContributions.none { it.family.familyId == family.familyId }
    }

    return FoodContributionReport(
        valueNoContribution = valueDonation.multiply(familiesWithNoContribution.size.toBigDecimal()),
        valuePendingContribution = valueDonation.multiply(familiesWithPendingContribution.size.toBigDecimal()),
        familiesWithContribution = familiesWithContribution.size,
        familiesWithNoContribution = familiesWithNoContribution.size,
        familiesWithPendingContribution = familiesWithPendingContribution.size,
        totalActiveFamilies = familiesWithContribution.size + familiesWithNoContribution.size + familiesWithPendingContribution.size,
        familiesWithContributionList = foodContributions.filter { it.wasDelivered == true && it.donationDate != null }
            .map { foodContribution ->
                FamiliesWithContribution(
                    responsibleName = foodContribution.family.responsibleName,
                    leaderName = foodContribution.leader.leaderName,
                    leaderColor = Color.valueOf(foodContribution.leader.leaderColor),
                )
            },
        familiesWithNoContributionList = families.filter { family ->
            foodContributions.none { it.family.familyId == family.familyId }
        }.map { family ->
            FamiliesWithNoContribution(
                responsibleName = family.responsibleName,
                neighborhoodName = family.neighborhood.neighborhoodName,
                leaderName = family.leadership.leaderName,
                totalChildren = family.totalChildren?.size ?: 0,
            )
        },
        familiesWithPendingContributionList = foodContributions.filter { it.wasDelivered == false && it.donationDate == null }
            .map { foodContribution ->
                FamiliesWithPendingContribution(
                    responsibleName = foodContribution.family.responsibleName,
                    totalChildren = foodContribution.family.totalChildren?.size ?: 0,
                    sponsorName = foodContribution.sponsor.sponsorName,
                    sponsorPhone = foodContribution.sponsor.sponsorPhone,
                    paidInSpecies = foodContribution.paidInSpecies,
                    leaderName = foodContribution.leader.leaderName
                )
            },
    )
}
