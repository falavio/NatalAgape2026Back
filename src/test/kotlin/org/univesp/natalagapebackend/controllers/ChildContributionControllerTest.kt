package org.univesp.natalagapebackend.controllers


import org.junit.jupiter.api.BeforeEach
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity
import org.univesp.natalagapebackend.dto.*
import org.univesp.natalagapebackend.models.*
import org.univesp.natalagapebackend.services.ChildContributionService
import java.time.LocalDate
import java.time.Year
import java.util.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildContributionControllerTest {

    private lateinit var childContributionService: ChildContributionService
    private lateinit var childContributionController: ChildContributionController

    private val family = Family(
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
    private val campaign = Campaign(
        campaignId = 1,
        campaignYear = Year.now(),
        campaignChurch = "Campaign Name"
    )
    private val leadership = Leadership(
        leaderId = 1,
        leaderName = "Leader Name",
        leaderPhone = "123456789",
        leaderRole = Role.LEADER,
        leaderColor = "BLACK",
        userName = "username",
        password = "password"
    )
    private val sponsor = Sponsor(
        sponsorId = 2,
        sponsorName = "Sponsor Name",
        sponsorPhone = "123456789",
        sponsorAddress = "Sponsor Address"
    )
    private val child = Child(
        childId = 1,
        childName = "Maria",
        gender = "Feminino",
        age = 11,
        clothes = "Camiseta tamanho M",
        shoes = "Tênis tamanho 34",
        pictureUrl = "https://example.com/maria.jpg",
        family = family
    )
    private val observation1 = "Observation 1"
    private val observation2 = "Observation 2"
    val donationDate1 = LocalDate.parse("2023-12-01")
    val donationDate2 = null

    @BeforeEach
    fun setUp() {
        childContributionService = mock(ChildContributionService::class.java)
        childContributionController = ChildContributionController(childContributionService)


    }

    @Test
    fun listAllReturnsChildContributions() {

        val contributions = listOf(
            ChildContribution(1, campaign, sponsor, leadership, child, true, true,donationDate1, observation1),
            ChildContribution(2, campaign, sponsor, leadership, child, false, false,donationDate2, observation2)
        )
        `when`(childContributionService.listAll()).thenReturn(contributions)

        val result = childContributionController.listAll()

        val expected = contributions.map { toDTOResponse(it) }

        assertEquals(ResponseEntity.ok(expected), result)
    }

    @Test
    fun findByIdReturnsChildContribution() {
        val contribution = Optional.of(
            ChildContribution(1, campaign, sponsor, leadership, child, true, true,donationDate1, "Observation")
        )
        `when`(childContributionService.findById(1)).thenReturn(contribution)

        val result = childContributionController.findById(1)

        assertEquals(ResponseEntity.ok(toDTOEditResponse(contribution.get())), result)
    }

    @Test
    fun findByIdReturnsNotFound() {
        `when`(childContributionService.findById(999L)).thenReturn(Optional.empty())

        val result = childContributionController.findById(999L)

        assertEquals(ResponseEntity.notFound().build(), result)

    }

    @Test
    fun saveCreatesChildContribution() {
        val request = ChildContributionRequest(1, 1, 1, 1,1, true, true,donationDate1.toString(), "Observation")
        val savedContribution = ChildContribution(1, campaign, sponsor, leadership, child, true,true, donationDate1, "Observation")

        `when`(childContributionService.save(request)).thenReturn(savedContribution)

        val result = childContributionController.save(request)

        assertEquals(ResponseEntity.ok(savedContribution), result)
    }

    @Test
    fun updateModifiesChildContribution() {
        val existingContribution = ChildContribution(1, campaign, sponsor, leadership, child, false, false,null, "Old Observation")
        val request = ChildContributionRequest(1, 1, 2, 1, 1, true, true,"Updated Observation")
        val updatedContribution =
            ChildContribution(1, campaign, sponsor, leadership, child, true, true,null, "Updated Observation")

        `when`(childContributionService.findById(1)).thenReturn(Optional.of(existingContribution))
        `when`(childContributionService.update(1, request)).thenReturn(updatedContribution)

        val result = childContributionController.update(1, request)

        assertEquals(ResponseEntity.ok(updatedContribution), result)
    }

    @Test
    fun updateReturnsNotFound() {
        val request = ChildContributionRequest(1, 2, 3, 1, 1, true, true,"2023-12-01", "Updated Observation")
        `when`(childContributionService.findById(999L)).thenReturn(Optional.empty())

        val result = childContributionController.update(999L, request)

        assertEquals(ResponseEntity.notFound().build(), result)
    }

    @Test
    fun reportReturnsChildIdAndFamilyIdInAllLists() {
        val contributionWithDelivery = ChildContribution(
            1, campaign, sponsor, leadership, child, true, true, donationDate1, observation1
        )
        val contributionPending = ChildContribution(
            2, campaign, sponsor, leadership, child, false, false, null, observation2
        )

        val report = childContributionToDTOReport(
            listOf(contributionWithDelivery),
            listOf(child)
        )

        // Verifica childId e familyId na lista com contribuicao
        assertEquals(1, report.childrenWithContributionList.size)
        assertEquals(child.childId, report.childrenWithContributionList[0].childId)
        assertEquals(family.familyId, report.childrenWithContributionList[0].familyId)

        val reportPending = childContributionToDTOReport(
            listOf(contributionPending),
            listOf(child)
        )

        // Verifica childId e familyId na lista pendente
        assertEquals(1, reportPending.childrenWithPendingContributionList.size)
        assertEquals(child.childId, reportPending.childrenWithPendingContributionList[0].childId)
        assertEquals(family.familyId, reportPending.childrenWithPendingContributionList[0].familyId)

        val child2 = child.copy(childId = 2, childName = "Joao")
        val reportNoContribution = childContributionToDTOReport(
            emptyList(),
            listOf(child2)
        )

        // Verifica childId e familyId na lista sem contribuicao
        assertEquals(1, reportNoContribution.childrenWithNoContributionList.size)
        assertEquals(child2.childId, reportNoContribution.childrenWithNoContributionList[0].childId)
        assertEquals(family.familyId, reportNoContribution.childrenWithNoContributionList[0].familyId)
    }
}