package org.univesp.natalagapebackend.services

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.univesp.natalagapebackend.dto.ChildRequest
import org.univesp.natalagapebackend.models.Child
import org.univesp.natalagapebackend.models.Color
import org.univesp.natalagapebackend.models.Campaign
import org.univesp.natalagapebackend.models.Family
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.models.Neighborhood
import org.univesp.natalagapebackend.models.Role
import org.univesp.natalagapebackend.repositories.ChildRepository
import java.time.LocalDate
import java.time.Year
import java.util.Optional
import kotlin.test.Test
import kotlin.test.assertEquals

class ChildServiceTest {
    private lateinit var childService: ChildService
    private lateinit var childRepository: ChildRepository
    private lateinit var familyService: FamilyService

        private val testCampaign = Campaign(1, Year.now(), "Test Church")

    @BeforeEach
    fun setUp() {
        childRepository = mock(ChildRepository::class.java)
        familyService = mock(FamilyService::class.java)
        childService = ChildService(childRepository, familyService)
    }

    @Test
    fun saveThrowsExceptionWhenFamilyNotFound() {
        val childRequest = ChildRequest(1, "Child 1", "2020-01-01", 1,"Male", "Clothes", "Shoes",  999 )
        `when`(familyService.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows<IllegalArgumentException> {
            childService.save(childRequest)
        }

        assertEquals("Family not found", exception.message)
    }

    @Test
    fun updateThrowsExceptionWhenChildNotFound() {
        val childRequest = ChildRequest(999, "Child 1", "2020-01-01", 1,"Male", "Clothes", "Shoes",  1)
        `when`(childRepository.findById(999)).thenReturn(Optional.empty())

        val exception = assertThrows<IllegalArgumentException> {
            childService.update(childRequest)
        }

        assertEquals("Child not found", exception.message)
    }

    @Test
    fun deleteByFamilyIdDeactivatesAllChildren() {
        val family = Family(
            1, "Family 1", "Address", "City", Neighborhood(1, "Centro"), null, Leadership(
                1, "Leader 1", "9999",
                Role.ADMIN, Color.BLACK.toString(),
                userName = "username",
                password = "password"
            ), testCampaign, true
        )
        val children = listOf(
            Child(1, "Child 1", "Male", 1, "Clothes", "Shoes", null, true, family),
            Child(2, "Child 2", "Female", 1, "Clothes", "Shoes", null, true, family)
        )
        `when`(childRepository.findAllByFamilyId(1)).thenReturn(children)

        childService.deleteByFamilyId(1)

        verify(childRepository).deactivateChild(1)
        verify(childRepository).deactivateChild(2)
    }

    @Test
    fun listAllReturnsEmptyListWhenNoActiveChildrenExist() {
        `when`(childRepository.findAllActive()).thenReturn(emptyList())

        val result = childService.listAll()

        assertTrue(result.isEmpty())
    }
}