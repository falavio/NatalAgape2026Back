package org.univesp.natalagapebackend.services

import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.univesp.natalagapebackend.dto.ChildContributionRequest
import org.univesp.natalagapebackend.handler.MaxContributionException
import org.univesp.natalagapebackend.models.Campaign
import org.univesp.natalagapebackend.models.Child
import org.univesp.natalagapebackend.models.ChildContribution
import org.univesp.natalagapebackend.models.Family
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.models.Neighborhood
import org.univesp.natalagapebackend.models.Role
import org.univesp.natalagapebackend.models.Sponsor
import org.univesp.natalagapebackend.repositories.ChildContributionRepository
import java.time.LocalDate
import java.time.Year
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildContributionServiceTest {
    private lateinit var childContributionService: ChildContributionService
    private lateinit var childContributionRepository: ChildContributionRepository
    private lateinit var campaignService: CampaignService
    private lateinit var childService: ChildService
    private lateinit var leadershipService: LeadershipService
    private lateinit var sponsorService: SponsorService

    @BeforeEach
    fun setUp() {
        childContributionRepository = mock(ChildContributionRepository::class.java)
        campaignService = mock(CampaignService::class.java)
        childService = mock(ChildService::class.java)
        leadershipService = mock(LeadershipService::class.java)
        sponsorService = mock(SponsorService::class.java)
        childContributionService = ChildContributionService(
            childContributionRepository,
            campaignService,
            sponsorService,
            childService,
            leadershipService,
        )
    }

    @Test
    fun saveThrowsExceptionWhenChildAlreadyHasContributionForCampaign() {
        val childContributionRequest = ChildContributionRequest(1, 1, 1, 1, 1, false, true, null, "Observation")
        val campaign = Campaign(1, Year.now(), "Campaign 1")
        val leadership = Leadership(1, "Leader 1", "123456789", Role.LEADER, "BLACK",
            userName = "username",
            password = "password")
        val family =
            Family(1, "Family 1", "123456789", "Address 1", Neighborhood(1, "City 1"), null, leadership,true, null)
        val child = Child(1, "Child 1", "Male", LocalDate.now(), "Clothes", "Shoes", null, true, family)
        val sponsor = Sponsor(1, "Sponsor 1", "123456789", "Address 1")

        `when`(campaignService.findById(1)).thenReturn(Optional.of(campaign))
        `when`(childService.findById(1)).thenReturn(Optional.of(child))
        `when`(leadershipService.findById(1)).thenReturn(Optional.of(leadership))
        `when`(sponsorService.findById(1)).thenReturn(Optional.of(sponsor))
        `when`(childContributionRepository.findChildrenContributionByCampaignId(1))
            .thenReturn(listOf(ChildContribution(1, campaign, sponsor, leadership, child,false, false,null, "Observation")))

        val exception = assertThrows<MaxContributionException> {
            childContributionService.save(childContributionRequest)
        }

        assertEquals("Child contribution already exists for this child and campaign", exception.message)
    }

    @Test
    fun updateThrowsExceptionWhenChildContributionNotFound() {
        val childContributionRequest = ChildContributionRequest(999, 1, 1, 1, 1, false, true, null, "Observation")
        `when`(childContributionRepository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            childContributionService.update(999, childContributionRequest)
        }

        assertEquals("Child contribution not found", exception.message)
    }
}