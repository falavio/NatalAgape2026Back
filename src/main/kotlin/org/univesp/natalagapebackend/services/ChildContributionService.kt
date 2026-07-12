package org.univesp.natalagapebackend.services

import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.univesp.natalagapebackend.dto.*
import org.univesp.natalagapebackend.handler.MaxContributionException
import org.univesp.natalagapebackend.models.*
import org.univesp.natalagapebackend.repositories.ChildContributionRepository
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Service
class ChildContributionService(
    private val childContributionRepository: ChildContributionRepository,
    private val campaignService: CampaignService,
    private val sponsorService: SponsorService,
    private val childService: ChildService,
    private val leadershipService: LeadershipService
) {

    fun listAll(): List<ChildContribution> {
        val campaigns = campaignService.findAllByIsActive()
        return childContributionRepository.findAll().filter { childContribution ->
            campaigns.any { it.campaignId == childContribution.campaign.campaignId }
        }
    }

    fun findById(id: Long) = childContributionRepository.findById(id)

    fun save(request: ChildContributionRequest): ChildContribution {
        val campaign = findCampaign(request.campaignId)
        val leadership = findLeadership(request.leaderId)
        val sponsor = findSponsor(request.sponsorId)
        val child = findChild(request.childId)

        val childContribution = ChildContribution(
            id = request.id,
            campaign = campaign,
            sponsor = sponsor,
            child = child,
            leadership = leadership,
            wasDelivered = request.wasDelivered,
            acceptance = request.toLocalDate(),
            observation = request.observation,
            toyDelivered = request.isToyDelivered ?: false
        )
        if (checkIfChildContributionExists(child.childId, campaign.campaignId)) {
            throw MaxContributionException("Child contribution already exists for this child and campaign")
        }

        return childContributionRepository.save(childContribution)
    }

    fun update(id: Long, request: ChildContributionRequest): ChildContribution {
        val existingContribution = childContributionRepository.findById(id)
            .orElseThrow { EntityNotFoundException("Child contribution not found") }

        val campaign = findCampaign(request.campaignId)
        val sponsor = findSponsor(request.sponsorId)
        val child = findChild(request.childId)

        val updatedContribution = existingContribution.copy(
            campaign = campaign,
            sponsor = sponsor,
            child = child,
            wasDelivered = request.wasDelivered,
            acceptance = convertToLocalDate(request.acceptance),
            observation = request.observation,
            toyDelivered = request.isToyDelivered
        )

        if (existingContribution.child.childId != updatedContribution.child.childId &&
            checkIfChildContributionExists(child.childId, campaign.campaignId)
        ) {
            throw MaxContributionException("Child contribution already exists for this child and campaign")
        }

        return childContributionRepository.save(updatedContribution)
    }

    fun report(campaignId: Long): ChildContributionReport {
        val campaign = campaignService.findById(campaignId).orElseThrow {
            EntityNotFoundException("Campaign not found")
        }
        val children = childService.listAll()

        val childrenContributions =
            childContributionRepository.findChildrenContributionByCampaignId(campaign.campaignId)
                .map { childrenContribution ->

                    ChildContribution(
                        id = childrenContribution.id,
                        campaign = childrenContribution.campaign,
                        sponsor = childrenContribution.sponsor,
                        child = childrenContribution.child,
                        leadership = childrenContribution.leadership,
                        wasDelivered = childrenContribution.wasDelivered,
                        acceptance = childrenContribution.acceptance,
                        observation = childrenContribution.observation,
                        toyDelivered = childrenContribution.toyDelivered
                    )

                }
        return childContributionToDTOReport(childrenContributions, children)
    }

    private fun findCampaign(campaignId: Long): Campaign =
        campaignService.findById(campaignId).orElseThrow {
            EntityNotFoundException("Campaign not found")
        }

    private fun findSponsor(sponsorId: Long): Sponsor =
        sponsorService.findById(sponsorId).orElseThrow {
            EntityNotFoundException("Sponsor not found")
        }

    private fun findChild(childId: Long): Child =
        childService.findById(childId).orElseThrow {
            EntityNotFoundException("Child not found")
        }

    private fun findLeadership(leadershipId: Long): Leadership =
        leadershipService.findById(leadershipId).orElseThrow {
            EntityNotFoundException("Leadership not found")
        }

    private fun checkIfChildContributionExists(
        childId: Long,
        campaignId: Long
    ): Boolean {
        return childContributionRepository.findChildrenContributionByCampaignId(campaignId)
            .any { it.child.childId == childId }
    }

    private fun convertToLocalDate(dateString: String?): LocalDate? {
        return try {
            dateString?.let { LocalDate.parse(it) }
        } catch (e: DateTimeParseException) {
            null
        }
    }
}