import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity
import org.univesp.natalagapebackend.controllers.NeighborhoodController
import org.univesp.natalagapebackend.models.Neighborhood
import org.univesp.natalagapebackend.services.NeighborhoodService
import java.util.*

class NeighborhoodControllerTest {

    private lateinit var neighborhoodService: NeighborhoodService
    private lateinit var neighborhoodController: NeighborhoodController

    @BeforeEach
    fun setUp() {
        neighborhoodService = mock(NeighborhoodService::class.java)
        neighborhoodController = NeighborhoodController(neighborhoodService)
    }

    @Test
    fun listAllReturnsNeighborhoods() {
        val neighborhoods = listOf(Neighborhood(1, "Neighborhood 1"), Neighborhood(2, "Neighborhood 2"))
        `when`(neighborhoodService.listAll()).thenReturn(neighborhoods)

        val result = neighborhoodController.listAll()

        assertEquals(neighborhoods, result)
    }

    @Test
    fun findByIdReturnsNeighborhood() {
        val neighborhood = Neighborhood(1, "Neighborhood 1")
        `when`(neighborhoodService.findById(1)).thenReturn(Optional.of(neighborhood))

        val result = neighborhoodController.findById(1)

        assertEquals(ResponseEntity.ok(neighborhood), result)
    }

    @Test
    fun findByIdReturnsNullForNonExistentId() {
        `when`(neighborhoodService.findById(999)).thenReturn(Optional.empty())

        val result = neighborhoodController.findById(999)

        assertEquals(ResponseEntity.notFound().build<Neighborhood>(), result)

    }

    @Test
    fun saveCreatesNeighborhood() {
        val neighborhood = Neighborhood(1, "New Neighborhood")
        `when`(neighborhoodService.save(neighborhood)).thenReturn(neighborhood)

        val result = neighborhoodController.save(neighborhood)

        assertEquals(neighborhood, result)
    }

    @Test
    fun updateModifiesNeighborhood() {
        val neighborhood = Neighborhood(1, "Updated Neighborhood")
        `when`(neighborhoodService.findById(1)).thenReturn(Optional.of(neighborhood))
        `when`(neighborhoodService.update(neighborhood)).thenReturn(neighborhood)

        val result = neighborhoodController.update(1L, neighborhood)

        assertEquals(ResponseEntity.ok(neighborhood), result)
    }

    @Test
    fun updateModifiesNeighborhoodNonExistent() {
        val neighborhood = Neighborhood(1, "Updated Neighborhood")
        `when`(neighborhoodService.findById(1)).thenReturn(Optional.empty())

        val result = neighborhoodController.update(999L, neighborhood)

        assertEquals(ResponseEntity.notFound().build<Neighborhood>(), result)
    }
}