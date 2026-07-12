package org.univesp.natalagapebackend.controllers

import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity
import org.univesp.natalagapebackend.dto.FoodContributionRequest
import org.univesp.natalagapebackend.dto.toDTOEditResponse
import org.univesp.natalagapebackend.dto.toDTOResponse
import org.univesp.natalagapebackend.models.Campaign
import org.univesp.natalagapebackend.models.Color
import org.univesp.natalagapebackend.models.Family
import org.univesp.natalagapebackend.models.FoodContribution
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.models.Neighborhood
import org.univesp.natalagapebackend.models.Role
import org.univesp.natalagapebackend.models.Sponsor
import org.univesp.natalagapebackend.services.FoodContributionService
import java.time.LocalDate
import java.time.Year
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals

class FoodContributionControllerTest {

    private lateinit var foodContributionService: FoodContributionService
    private lateinit var foodContributionController: FoodContributionController

    @BeforeEach
    fun setUp() {
        foodContributionService = mock(FoodContributionService::class.java)
        foodContributionController = FoodContributionController(foodContributionService)
    }

    @Test
    fun listAllReturnsFoodContributions() {
        val campaign = Campaign(
            1,
            Year.now(),
            "Church"
        )

        val sponsor = Sponsor(
            1,
            "Sponsor Name",
            "Sponsor Phone",
            "Sponsor Address"
        )

        val family = Family(
            1,
            "Family Name",
            "123456789",
            "123 Street",
            Neighborhood(1, "Centro"),
            "No observation",
            Leadership(
                leaderId = 1,
                leaderName = "Leader 1",
                leaderPhone = "123456789",
                leaderRole = Role.LEADER,
                leaderColor = "BLACK",
                userName = "username",
                password = "password"
            )
        )

        val leadership = Leadership(1, "Leader1", "123456789", Role.ADMIN, Color.RED.toString(),
            userName = "username",
            password = "password")

        val foodContributions = listOf(
            FoodContribution(1, campaign, leadership, sponsor, family, false, false, null, null),
            FoodContribution(2, campaign, leadership, sponsor, family, true, true, LocalDate.now(), null),
        )
        `when`(foodContributionService.listAll()).thenReturn(foodContributions)

        val result = foodContributionController.listAll()

        val expected = foodContributions.map { toDTOResponse(it) }

        assertEquals(ResponseEntity.ok(expected), result)
    }

    @Test
    fun findByIdReturnsFoodContribution() {
        val campaign = Campaign(
            1,
            Year.now(),
            "Church"
        )

        val sponsor = Sponsor(
            1,
            "Sponsor Name",
            "Sponsor Phone",
            "Sponsor Address"
        )

        val family = Family(
            1,
            "Family Name",
            "123456789",
            "123 Street",
            Neighborhood(1, "Centro"),
            "No observation",
            Leadership(
                leaderId = 1,
                leaderName = "Leader 1",
                leaderPhone = "123456789",
                leaderRole = Role.LEADER,
                leaderColor = "BLACK",
                userName = "username",
                password = "password"
            )
        )
        val leadership = Leadership(1, "Leader1", "123456789", Role.ADMIN, Color.RED.toString(),
            userName = "username",
            password = "password")

        val foodContribution = Optional.of(
            FoodContribution(1, campaign, leadership, sponsor, family, true, true, LocalDate.now(), null),
        )
        `when`(foodContributionService.findById(1)).thenReturn(foodContribution)

        val result = foodContributionController.findById(1)

        assertEquals(ResponseEntity.ok(toDTOEditResponse(foodContribution.get())), result)
    }

    @Test
    fun findByIdReturnsEmptyForNonExistentId() {
        `when`(foodContributionService.findById(999L)).thenReturn(Optional.empty())

        val result = foodContributionController.findById(999L)

        assertEquals(ResponseEntity.notFound().build(), result)
    }

    @Test
    fun saveCreatesFoodContribution() {
        val campaign = Campaign(
            1,
            Year.now(),
            "Church"
        )

        val sponsor = Sponsor(
            1,
            "Sponsor Name",
            "Sponsor Phone",
            "Sponsor Address"
        )

        val family = Family(
            1,
            "Family Name",
            "123456789",
            "123 Street",
            Neighborhood(1, "Centro"),
            "No observation",Leadership(
                leaderId = 1,
                leaderName = "Leader 1",
                leaderPhone = "123456789",
                leaderRole = Role.LEADER,
                leaderColor = "BLACK",
                userName = "username",
                password = "password"
            )
        )

        val leadership = Leadership(1, "Leader1", "123456789", Role.ADMIN, Color.RED.toString(),
            userName = "username",
            password = "password")

        val foodContributionRequest = FoodContributionRequest(
            campaignId = 1,
            sponsorId = 1,
            familyId = 1,
            leaderId = 1,
            wasDelivered = false,
            paidInSpecies = false,
            donationDate = null,
            observation = null
        )
        val savedFoodContribution =
            FoodContribution(2, campaign, leadership, sponsor, family, true, true, LocalDate.now(), null)

        `when`(foodContributionService.save(foodContributionRequest)).thenReturn(savedFoodContribution)

        val result = foodContributionController.save(foodContributionRequest)

        assertEquals(ResponseEntity.ok(savedFoodContribution), result)
    }

    @Test
    fun updateModifiesFoodContribution() {
        val campaign = Campaign(
            1,
            Year.now(),
            "Church"
        )

        val sponsor = Sponsor(
            1,
            "Sponsor Name",
            "Sponsor Phone",
            "Sponsor Address"
        )

        val family = Family(
            1,
            "Family Name",
            "123456789",
            "123 Street",
            Neighborhood(1, "Centro"),
            "No observation",Leadership(
                leaderId = 1,
                leaderName = "Leader 1",
                leaderPhone = "123456789",
                leaderRole = Role.LEADER,
                leaderColor = "BLACK",
                userName = "username",
                password = "password"
            )
        )

        val leadership = Leadership(1, "Leader1", "123456789", Role.ADMIN, Color.RED.toString(),
            userName = "username",
            password = "password")

        val existingFoodContribution =
            FoodContribution(1, campaign, leadership, sponsor, family, false, false, null, null)

        val foodContributionRequest = FoodContributionRequest(
            id = 1,
            campaignId = 1,
            sponsorId = 1,
            familyId = 1,
            leaderId = 1,
            wasDelivered = true,
            paidInSpecies = true,
            donationDate = LocalDate.now().toString(),
            observation = null
        )
        val updatedFoodContribution =
            FoodContribution(1, campaign, leadership, sponsor, family, true, true, LocalDate.now(), null)

        `when`(foodContributionService.findById(1)).thenReturn(Optional.of(existingFoodContribution))
        `when`(foodContributionService.update(foodContributionRequest)).thenReturn(updatedFoodContribution)

        val result = foodContributionController.update(1, foodContributionRequest)

        assertEquals(ResponseEntity.ok(updatedFoodContribution), result)
    }

    @Test
    fun updateReturnsNotFoundForNonExistentId() {
        val foodContributionRequest = FoodContributionRequest(
            campaignId = 1,
            sponsorId = 1,
            familyId = 1,
            leaderId = 1,
            wasDelivered = false,
            paidInSpecies = false,
            donationDate = null,
            observation = null
        )
        `when`(foodContributionService.findById(999L)).thenReturn(Optional.empty())

        val result = foodContributionController.update(999L, foodContributionRequest)

        assertEquals(ResponseEntity.notFound().build(), result)
    }
}