import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity
import org.univesp.natalagapebackend.controllers.FamilyController
import org.univesp.natalagapebackend.dto.*
import org.univesp.natalagapebackend.models.*
import org.univesp.natalagapebackend.models.DTO.FamilyDTOOutput
import org.univesp.natalagapebackend.models.DTO.toDTOOutput
import org.univesp.natalagapebackend.services.ChildService
import org.univesp.natalagapebackend.services.FamilyService
import java.time.LocalDate
import java.util.*

class FamilyControllerTest {

    private lateinit var familyService: FamilyService
    private lateinit var childService: ChildService
    private lateinit var familyController: FamilyController

    @BeforeEach
    fun setUp() {
        familyService = mock(FamilyService::class.java)
        childService = mock(ChildService::class.java)
        familyController = FamilyController(familyService, childService)
    }

    @Test
    fun listAllReturnsFamiliesWithChildren() {

        val family01 = Family(
            1,
            "Family 1",
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
        val family02 = Family(
            2, "Family 2", "123456789", "123 Street", Neighborhood(1, "Centro"), "No observation", Leadership(
                leaderId = 1,
                leaderName = "Leader 1",
                leaderPhone = "123456789",
                leaderRole = Role.LEADER,
                leaderColor = "BLACK",
                userName = "username",
                password = "password"
            )
        )

        val child = Child(
            1, "name", "Male", LocalDate.now(), "", "", null, true,family01
        )

        val families = listOf(family01, family02)
        `when`(familyService.listAll()).thenReturn(families)
        `when`(childService.findByFamilyId(family01.familyId)).thenReturn(listOf(child))
        `when`(childService.findByFamilyId(family02.familyId)).thenReturn(emptyList())

        val result = familyController.listAll()

        val expected = listOf(
            FamilyWithChildrenDTO(
                familyId = 1,
                responsibleName = "Family 1",
                phoneNumber = "123456789",
                address = "123 Street",
                neighborhoodId = 1,
                neighborhoodName = "Centro",
                observation = "No observation",
                children = listOf(
                    Children(
                        childId = 1,
                        childName = "name",
                        gender = "Male",
                        birthDate = LocalDate.now(),
                        clothes = "",
                        shoes = "",
                        pictureUrl = null
                    )
                ),
                leaderId = 1,
                leaderName = "Leader 1",
                pictureUrl = null,
                pictureSubscription = null
            ),
            FamilyWithChildrenDTO(
                familyId = 2,
                responsibleName = "Family 2",
                phoneNumber = "123456789",
                address = "123 Street",
                neighborhoodId = 1,
                neighborhoodName = "Centro",
                observation = "No observation",
                children = emptyList(),
                leaderId = 1,
                leaderName = "Leader 1",
                pictureUrl = null,
                pictureSubscription = null
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun listAllReturnsEmptyListWhenNoFamiliesExist() {
        `when`(familyService.listAll()).thenReturn(emptyList())

        val result = familyController.listAll()

        assertEquals(emptyList<FamilyWithChildrenDTO>(), result)
    }

    @Test
    fun findByIdReturnsFamilyWithChildren() {
        val family = Family(
            1, "Family 1", "123456789", "123 Street", Neighborhood(1, "Centro"), "No observation", Leadership(
                leaderId = 1,
                leaderName = "Leader 1",
                leaderPhone = "123456789",
                leaderRole = Role.LEADER,
                leaderColor = "BLACK",
                userName = "username",
                password = "password"
            )
        )
        val children = listOf(
            Child(1, "Child 1", "Male", LocalDate.now(), "Clothes", "Shoes", null,true, family)
        )
        `when`(familyService.findById(1)).thenReturn(Optional.of(family))
        `when`(childService.findByFamilyId(1)).thenReturn(children)

        val result = familyController.findById(1)

        val expected = ResponseEntity.ok(
            FamilyWithChildrenDTO(
                familyId = 1,
                responsibleName = "Family 1",
                phoneNumber = "123456789",
                address = "123 Street",
                neighborhoodId = 1,
                neighborhoodName = "Centro",
                observation = "No observation",
                children = children.map {
                    Children(
                        childId = it.childId,
                        childName = it.childName,
                        gender = it.gender,
                        birthDate = it.birthDate,
                        clothes = it.clothes,
                        shoes = it.shoes,
                        pictureUrl = it.pictureUrl
                    )
                },
                leaderId = 1,
                leaderName = "Leader 1",
                pictureUrl = null,
                pictureSubscription = null
            )
        )

        assertEquals(expected, result)
    }

    @Test
    fun findByIdReturnsNotFoundWhenFamilyDoesNotExist() {
        `when`(familyService.findById(999)).thenReturn(Optional.empty())

        val result = familyController.findById(999)

        assertEquals(ResponseEntity.notFound().build<FamilyWithChildrenDTO>(), result)
    }

    @Test
    fun findByIdReturnsNotFoundForNonExistentId() {
        `when`(familyService.findById(999)).thenReturn(Optional.empty())

        val result = familyController.findById(999)

        assertEquals(ResponseEntity.notFound().build<FamilyDTOOutput>(), result)
    }

    @Test
    fun saveCreatesFamily() {
        val familyInput = FamilyDTOInput(1, "New Family", "123456789", "123 Street", 1, "No observation", leaderId = 1)
        val familyOutput =
            FamilyDTOOutput(1, "New Family", "123456789", "123 Street", null, "Centro", "No observation", 1, "Leader 1")
        `when`(familyService.save(familyInput)).thenReturn(
            familyInput.toEntity(
                Neighborhood(1, "Centro"),
                Leadership(1, "Leader 1", "123456789", Role.LEADER, "BLACK",
                    userName = "username",
                    password = "password")
            )
        )

        val result = familyController.save(familyInput)

        assertEquals(ResponseEntity.ok(familyOutput), result)
    }

    @Test
    fun updateModifiesFamily() {
        val familyInput =
            FamilyDTOInput(1, "Updated Family", "123456789", "123 Street", 1, "No observation", leaderId = 1)
        val existingFamily =
            Family(
                1,
                "Existing Family",
                "987654321",
                "456 Avenue",
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
        val updatedFamily =
            Family(
                1, "Updated Family", "123456789", "123 Street", Neighborhood(1, "Centro"), "No observation", Leadership(
                    leaderId = 1,
                    leaderName = "Leader 1",
                    leaderPhone = "123456789",
                    leaderRole = Role.LEADER,
                    leaderColor = "BLACK",
                    userName = "username",
                    password = "password"
                )
            )
        `when`(familyService.findById(1)).thenReturn(Optional.of(existingFamily))
        `when`(familyService.update(familyInput.copy(familyId = 1))).thenReturn(updatedFamily)

        val result = familyController.update(1, familyInput)

        assertEquals(ResponseEntity.ok(updatedFamily.toDTOOutput()), result)
    }

    @Test
    fun updateReturnsNotFoundForNonExistentId() {
        val familyInput =
            FamilyDTOInput(1, "Updated Family", "123456789", "123 Street", 1, "No observation", leaderId = 1)
        `when`(familyService.findById(999)).thenReturn(Optional.empty())

        val result = familyController.update(999, familyInput)

        assertEquals(ResponseEntity.notFound().build<FamilyDTOOutput>(), result)
    }
}