package org.univesp.natalagapebackend.services

import jakarta.persistence.EntityNotFoundException
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.univesp.natalagapebackend.dto.FoodContributionRequest
import org.univesp.natalagapebackend.handler.MaxContributionException
import org.univesp.natalagapebackend.models.Campaign
import org.univesp.natalagapebackend.models.Color
import org.univesp.natalagapebackend.models.Family
import org.univesp.natalagapebackend.models.FoodContribution
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.models.Neighborhood
import org.univesp.natalagapebackend.models.Role
import org.univesp.natalagapebackend.models.Sponsor
import org.univesp.natalagapebackend.repositories.FoodContributionRepository
import java.time.Year
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals

class FoodContributionServiceTest {

    private lateinit var foodContributionService: FoodContributionService
    private lateinit var foodContributionRepository: FoodContributionRepository
    private lateinit var campaignService: CampaignService
    private lateinit var leadershipService: LeadershipService
    private lateinit var familyService: FamilyService
    private lateinit var sponsorService: SponsorService
    private lateinit var childService: ChildService

    @BeforeEach
    fun setUp() {
        foodContributionRepository = mock(FoodContributionRepository::class.java)
        campaignService = mock(CampaignService::class.java)
        familyService = mock(FamilyService::class.java)
        leadershipService = mock(LeadershipService::class.java)
        sponsorService = mock(SponsorService::class.java)
        childService = mock(ChildService::class.java)
        foodContributionService = FoodContributionService(
            foodContributionRepository,
            campaignService,
            leadershipService,
            familyService,
            sponsorService,
            childService
        )
    }

    @Test
    fun saveThrowsExceptionWhenCampaignNotFound() {
        val foodContributionRequest = FoodContributionRequest(1, 1, 1, 1, 1, false, false, null, "Observation")
        `when`(campaignService.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            foodContributionService.save(foodContributionRequest)
        }

        assertEquals("Campaign not found", exception.message)
    }

    @Test
    fun saveThrowsExceptionWhenFamilyNotFound() {
        val foodContributionRequest = FoodContributionRequest(1, 1, 1, 1, 1, false, false, null, "Observation")
        val campaign = Campaign(1, Year.now(), "church")
        val leadership = Leadership(1, "Leader 1", "123456789", Role.ADMIN, Color.RED.toString(),
            userName = "username",
            password = "password")
        `when`(campaignService.findById(1)).thenReturn(Optional.of(campaign))
        `when`(leadershipService.findById(1)).thenReturn(Optional.of(leadership))
        `when`(familyService.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            foodContributionService.save(foodContributionRequest)
        }

        assertEquals("Family not found", exception.message)
    }

    @Test
    fun saveThrowsExceptionWhenLeadershipNotFound() {
        val foodContributionRequest = FoodContributionRequest(1, 1, 1, 1, 1, false, false, null, "Observation")
        val campaign = Campaign(1, Year.now(), "church")
        val family = Family(1, "Family 1", "123456789", "123 Street", Neighborhood(1, "Centro"), "No observation",Leadership(
            leaderId = 1,
            leaderName = "Leader 1",
            leaderPhone = "123456789",
            leaderRole = Role.LEADER,
            leaderColor = "BLACK",
            userName = "username",
            password = "password"
        ))
        `when`(campaignService.findById(1)).thenReturn(Optional.of(campaign))
        `when`(familyService.findById(1)).thenReturn(Optional.of(family))
        `when`(leadershipService.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            foodContributionService.save(foodContributionRequest)
        }

        assertEquals("Leadership not found", exception.message)
    }

    @Test
    fun saveThrowsExceptionWhenSponsorNotFound() {
        val foodContributionRequest = FoodContributionRequest(1, 1, 999, 1, 1, false, false, null, "Observation")
        val campaign = Campaign(1, Year.now(), "Campaign 1")
        val family = Family(1, "Family 1", "123456789", "123 Street", Neighborhood(1, "Centro"), "No observation",Leadership(
            leaderId = 1,
            leaderName = "Leader 1",
            leaderPhone = "123456789",
            leaderRole = Role.LEADER,
            leaderColor = "BLACK",
            userName = "username",
            password = "password"
        ))
        val leadership = Leadership(1, "Leader 1", "123456789", Role.ADMIN, Color.RED.toString(),
            userName = "username",
            password = "password")
        `when`(campaignService.findById(1)).thenReturn(Optional.of(campaign))
        `when`(familyService.findById(1)).thenReturn(Optional.of(family))
        `when`(leadershipService.findById(1)).thenReturn(Optional.of(leadership))
        `when`(sponsorService.findById(1)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            foodContributionService.save(foodContributionRequest)
        }

        assertEquals("Sponsor not found", exception.message)
    }

    @Test
    fun saveThrowsExceptionWhenFamilyAlreadyHasContributionForCampaign() {
        val foodContributionRequest = FoodContributionRequest(1, 1, 1, 1, 1, false, false, null, "Observation")
        val campaign = Campaign(1, Year.now(), "Campaign 1")
        val family = Family(1, "Family 1", "123456789", "123 Street", Neighborhood(1, "Centro"), "No observation", Leadership(
            leaderId = 1,
            leaderName = "Leader 1",
            leaderPhone = "123456789",
            leaderRole = Role.LEADER,
            leaderColor = "BLACK",
            userName = "username",
            password = "password"
        ))
        val leadership = Leadership(1, "Leader 1", "123456789", Role.ADMIN, Color.RED.toString(),
            userName = "username",
            password = "password")
        val sponsor = Sponsor(1, "Sponsor 1", "123456789",  "123 Street")

        `when`(campaignService.findById(1)).thenReturn(Optional.of(campaign))
        `when`(familyService.findById(1)).thenReturn(Optional.of(family))
        `when`(leadershipService.findById(1)).thenReturn(Optional.of(leadership))
        `when`(sponsorService.findById(1)).thenReturn(Optional.of(sponsor))
        `when`(foodContributionRepository.findFoodContributionByCampaignId(1))
            .thenReturn(listOf(FoodContribution(1, campaign, leadership, sponsor, family,false, false, null, "Observation")))

        val exception = assertThrows<MaxContributionException> {
            foodContributionService.save(foodContributionRequest)
        }

        assertEquals("Family already has a contribution for this campaign", exception.message)
    }

    @Test
    fun updateThrowsExceptionWhenFoodContributionNotFound() {
        val foodContributionRequest = FoodContributionRequest(999, 1, 1, 1, 1, false, false, null, "Observation")
        `when`(foodContributionRepository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows<EntityNotFoundException> {
            foodContributionService.update(foodContributionRequest)
        }

        assertEquals("Food contribution not found", exception.message)
    }
}