package org.univesp.natalagapebackend.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.univesp.natalagapebackend.dto.FoodContributionReport
import org.univesp.natalagapebackend.dto.FoodContributionRequest
import org.univesp.natalagapebackend.dto.toDTOReport
import org.univesp.natalagapebackend.dto.toLocalDate
import org.univesp.natalagapebackend.handler.MaxContributionException
import org.univesp.natalagapebackend.models.FoodContribution
import org.univesp.natalagapebackend.repositories.FoodContributionRepository

@Service
class FoodContributionService(
    private val foodContributionRepository: FoodContributionRepository,
    private val campaignService: CampaignService,
    private val leadershipService: LeadershipService,
    private val familyService: FamilyService,
    private val sponsorService: SponsorService,
    private val childService: ChildService
) {

    fun listAll(): List<FoodContribution> {
        val campaigns = campaignService.findAllByIsActive()
        return foodContributionRepository.findAll().filter { foodContribution ->
            campaigns.any { it.campaignId == foodContribution.campaign.campaignId }
        }
    }

    fun findById(id: Long) = foodContributionRepository.findById(id)
    fun save(foodContribution: FoodContributionRequest): FoodContribution {
        val campaign = campaignService.findById(foodContribution.campaignId).orElseThrow {
            EntityNotFoundException("Campaign not found")
        }
        val leadership = leadershipService.findById(foodContribution.leaderId).orElseThrow {
            EntityNotFoundException("Leadership not found")
        }
        val family = familyService.findById(foodContribution.familyId).orElseThrow {
            EntityNotFoundException("Family not found")
        }
        val sponsor = sponsorService.findById(foodContribution.sponsorId).orElseThrow {
            EntityNotFoundException("Sponsor not found")
        }
        val foodContributionToSave = FoodContribution(
            id = foodContribution.id,
            campaign = campaign,
            family = family,
            leader = leadership,
            sponsor = sponsor,
            wasDelivered = foodContribution.wasDelivered,
            paidInSpecies = foodContribution.paidInSpecies,
            donationDate = foodContribution.toLocalDate(),
            observation = foodContribution.observation
        )

        if (checkCampaignFoodContributionExists(foodContribution.familyId, foodContribution.campaignId)) {
            throw MaxContributionException("Family already has a contribution for this campaign")
        }

        return foodContributionRepository.save(foodContributionToSave)
    }

    fun update(foodContribution: FoodContributionRequest): FoodContribution {
        val existingFoodContribution = foodContributionRepository.findById(foodContribution.id)
            .orElseThrow { EntityNotFoundException("Food contribution not found") }

        val campaign = campaignService.findById(foodContribution.campaignId).orElseThrow {
            EntityNotFoundException("Campaign not found")
        }
        val leadership = leadershipService.findById(foodContribution.leaderId).orElseThrow {
            EntityNotFoundException("Leadership not found")
        }
        val family = familyService.findById(foodContribution.familyId).orElseThrow {
            EntityNotFoundException("Family not found")
        }
        val sponsor = sponsorService.findById(foodContribution.sponsorId).orElseThrow {
            EntityNotFoundException("Sponsor not found")
        }

        val donationToUpdate = FoodContribution(
            id = existingFoodContribution.id,
            campaign = campaign,
            family = family,
            sponsor = sponsor,
            leader = leadership,
            wasDelivered = foodContribution.wasDelivered,
            paidInSpecies = foodContribution.paidInSpecies,
            donationDate = foodContribution.toLocalDate(),
            observation = foodContribution.observation
        )

        if (existingFoodContribution.family.familyId != donationToUpdate.family.familyId &&
            checkCampaignFoodContributionExists(foodContribution.familyId, foodContribution.campaignId)) {
            throw MaxContributionException("Family already has a contribution for this campaign")
        }

        return foodContributionRepository.save(donationToUpdate)
    }

    fun report(campaignId: Long): FoodContributionReport {
        val campaign = campaignService.findById(campaignId).orElseThrow {
            EntityNotFoundException("Campaign not found")
        }

        val families = familyService.listAll().map { family ->
            val children = childService.findByFamilyId(family.familyId)
            family.copy(totalChildren = children)
        }
        val foodContribution =
            foodContributionRepository.findFoodContributionByCampaignId(campaign.campaignId).map { foodContribution ->
                val family = families.find { it.familyId == foodContribution.family.familyId }
                val children = childService.findByFamilyId(foodContribution.family.familyId)
                foodContribution.copy(family = family?.copy(totalChildren = children) ?: foodContribution.family)
            }

        return toDTOReport(foodContribution, families)
    }

    private fun checkCampaignFoodContributionExists(familyId: Long, campaignId: Long): Boolean {
        return foodContributionRepository
            .findFoodContributionByCampaignId(campaignId)
            .any { it.family.familyId == familyId }
    }
}