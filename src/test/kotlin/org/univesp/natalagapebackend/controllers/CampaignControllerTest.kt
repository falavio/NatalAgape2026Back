import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.*
import org.springframework.http.ResponseEntity
import org.univesp.natalagapebackend.controllers.CampaignController
import org.univesp.natalagapebackend.models.Campaign
import org.univesp.natalagapebackend.services.CampaignService
import java.time.Year
import java.util.*

class CampaignControllerTest {

    private lateinit var campaignService: CampaignService
    private lateinit var campaignController: CampaignController

    @BeforeEach
    fun setUp() {
        campaignService = mock(CampaignService::class.java)
        campaignController = CampaignController(campaignService)
    }

    @Test
    fun listAllReturnsCampaigns() {
        val campaigns = listOf(Campaign(1, Year.of(2025), "Test 1"), Campaign(2, Year.of(2024), "Test 2"))
        `when`(campaignService.findAllByIsActive()).thenReturn(campaigns)

        val result = campaignController.listAll()

        assertEquals(campaigns, result)
    }

    @Test
    fun findByIdReturnsCampaign() {
        val campaign = Optional.of(Campaign(1, Year.of(2025), "Test 1"))
        `when`(campaignService.findById(1)).thenReturn(campaign)

        val result = campaignController.findById(1)

        assertEquals(ResponseEntity.ok(campaign.get()), result)
    }

    @Test
    fun findByIdReturnsNullForNonExistentId() {
        `when`(campaignService.findById(999L)).thenReturn(Optional.empty())

        val result = campaignController.findById(999L)

        assertEquals(ResponseEntity.notFound().build<Campaign>(), result)
    }

    @Test
    fun saveCreatesCampaign() {
        val campaign = Campaign(1, Year.of(2025), "Test 1")
        `when`(campaignService.save(campaign)).thenReturn(campaign)

        val result = campaignController.save(campaign)

        assertEquals(campaign, result)
    }

    @Test
    fun updateModifiesCampaign() {
        val campaign = Campaign(1L, Year.of(2025), "Test 1")
        `when`(campaignService.findById(1L)).thenReturn(Optional.of(campaign))
        `when`(campaignService.update(campaign)).thenReturn(campaign)

        val result = campaignController.update(1L, campaign)

        assertEquals(ResponseEntity.ok(campaign), result)
    }

    @Test
    fun updateModifiesCampaignIdNonExistent() {
        val campaign = Campaign(1L, Year.of(2025), "Test 1")
        `when`(campaignService.findById(1L)).thenReturn(Optional.empty())

        val result = campaignController.update(999L, campaign)

        assertEquals(ResponseEntity.notFound().build<Campaign>(), result)
    }
}