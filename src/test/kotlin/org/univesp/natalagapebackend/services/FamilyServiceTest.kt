import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.univesp.natalagapebackend.dto.FamilyDTOInput
import org.univesp.natalagapebackend.models.Family
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.models.Neighborhood
import org.univesp.natalagapebackend.models.Role
import org.univesp.natalagapebackend.repositories.FamilyRepository
import org.univesp.natalagapebackend.services.FamilyService
import org.univesp.natalagapebackend.services.LeadershipService
import org.univesp.natalagapebackend.services.NeighborhoodService
import java.util.*

class FamilyServiceTest {

    private lateinit var familyRepository: FamilyRepository
    private lateinit var neighborhoodService: NeighborhoodService
    private lateinit var familyService: FamilyService
    private lateinit var leadershipService: LeadershipService


    @BeforeEach
    fun setUp() {
        familyRepository = mock(FamilyRepository::class.java)
        neighborhoodService = mock(NeighborhoodService::class.java)
        leadershipService = mock(LeadershipService::class.java)
        familyService = FamilyService(familyRepository, neighborhoodService, leadershipService)
    }

    @Test
    fun listAllReturnsAllFamilies() {
        val families = listOf(
            Family(
                1, "Family 1",
                phoneNumber = "123456789",
                address = "123 Street",
                neighborhood = Neighborhood(1, "Centro"),
                observation = null,
                leadership = Leadership(
                    leaderId = 1,
                    leaderName = "Leader 1",
                    leaderPhone = "123456789",
                    leaderRole = Role.LEADER,
                    leaderColor = "BLACK",
                    userName = "username",
                    password = "password"
                )

            ), Family(
                2, "Family 2",
                phoneNumber = "987654321",
                address = "456 Avenue",
                neighborhood = Neighborhood(2, "Bairro"),
                observation = "No observation",
                leadership = Leadership(
                    leaderId = 2,
                    leaderName = "Leader 2",
                    leaderPhone = "123456789",
                    leaderRole = Role.LEADER,
                    leaderColor = "WHITE",
                    userName = "username",
                    password = "password"
                )
            )
        )
        `when`(familyRepository.findAllActive()).thenReturn(families)

        val result = familyService.listAll()

        assertEquals(families, result)
    }

    @Test
    fun findByIdReturnsFamily() {
        val family = Family(
            1, "Family 1",
            phoneNumber = "123456789",
            address = "123 Street",
            neighborhood = Neighborhood(1, "Centro"),
            observation = null,
            leadership = Leadership(
                leaderId = 1,
                leaderName = "Leader 1",
                leaderPhone = "123456789",
                leaderRole = Role.LEADER,
                leaderColor = "BLACK",
                userName = "username",
                password = "password"
            )
        )
        `when`(familyRepository.findById(1)).thenReturn(Optional.of(family))

        val result = familyService.findById(1)

        assertEquals(Optional.of(family), result)
    }

    @Test
    fun findByIdReturnsEmptyForNonExistentId() {
        `when`(familyRepository.findById(999)).thenReturn(Optional.empty())

        val result = familyService.findById(999)

        assertEquals(Optional.empty<Family>(), result)
    }

    @Test
    fun saveCreatesFamily() {
        val familyDTO = FamilyDTOInput(1, "New Family", "123456789", "Address", 1, "Observation", leaderId = 1)
        val neighborhood = Neighborhood(1, "Neighborhood 1")
        val leadership = Leadership(
            leaderId = 1,
            leaderName = "Leader 1",
            leaderPhone = "123456789",
            leaderRole = Role.LEADER,
            leaderColor = "BLACK",
            userName = "username",
            password = "password"
        )
        val family = Family(
            1, "New Family", "123456789", "Address", neighborhood, "Observation", leadership
        )
        `when`(neighborhoodService.findById(1)).thenReturn(Optional.of(neighborhood))
        `when`(leadershipService.findById(1)).thenReturn(Optional.of(leadership))
        `when`(familyRepository.save(any(Family::class.java))).thenReturn(family)

        val result = familyService.save(familyDTO)

        assertEquals(family, result)
    }

    @Test
    fun saveThrowsExceptionForNonExistentNeighborhood() {
        val familyDTO = FamilyDTOInput(999, "New Family", "123456789", "Address", 1, "Observation", leaderId = 1)
        `when`(neighborhoodService.findById(999)).thenReturn(Optional.empty())

        assertThrows(IllegalArgumentException::class.java) {
            familyService.save(familyDTO)
        }
    }

    @Test
    fun updateModifiesFamily() {
        val familyDTO =
            FamilyDTOInput(1, "Updated Family", "987654321", "New Address", 1, "New Observation", leaderId = 1)
        val existingFamily =
            Family(
                1,
                "Existing Family",
                "123456789",
                "Address",
                Neighborhood(1, "Neighborhood 1"),
                "Observation",
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
        val updatedFamily = Family(
            1,
            "Updated Family",
            "987654321",
            "New Address",
            Neighborhood(1, "Neighborhood 1"),
            "New Observation",
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
        `when`(familyRepository.findById(1)).thenReturn(Optional.of(existingFamily))
        `when`(neighborhoodService.findById(1)).thenReturn(Optional.of(Neighborhood(1, "Neighborhood 1")))
        `when`(leadershipService.findById(1)).thenReturn(Optional.of(Leadership(
            leaderId = 1,
            leaderName = "Leader 1",
            leaderPhone = "123456789",
            leaderRole = Role.LEADER,
            leaderColor = "BLACK",
            userName = "username",
            password = "password"
        )))
        `when`(familyRepository.save(any(Family::class.java))).thenReturn(updatedFamily)

        val result = familyService.update(familyDTO)

        assertEquals(updatedFamily, result)
    }

    @Test
    fun updateThrowsExceptionForNonExistentFamily() {
        val familyDTO = FamilyDTOInput(1, "Updated Family", "987654321", "New Address", 999, "New Observation",leaderId =1)
        `when`(familyRepository.findById(999)).thenReturn(Optional.empty())

        assertThrows(IllegalArgumentException::class.java) {
            familyService.update(familyDTO)
        }
    }

    @Test
    fun updateThrowsExceptionForNonExistentNeighborhood() {
        val familyDTO = FamilyDTOInput(1, "Updated Family", "987654321", "New Address", 1, "New Observation", leaderId = 1)
        val existingFamily =
            Family(1, "Existing Family", "123456789", "Address", Neighborhood(1, "Neighborhood 1"), "Observation",Leadership(
                leaderId = 1,
                leaderName = "Leader 1",
                leaderPhone = "123456789",
                leaderRole = Role.LEADER,
                leaderColor = "BLACK",
                userName = "username",
                password = "password"
            ))
        `when`(familyRepository.findById(1)).thenReturn(Optional.of(existingFamily))
        `when`(neighborhoodService.findById(1)).thenReturn(Optional.empty())

        assertThrows(IllegalArgumentException::class.java) {
            familyService.update(familyDTO)
        }
    }
}