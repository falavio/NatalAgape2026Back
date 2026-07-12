package org.univesp.natalagapebackend.services

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.univesp.natalagapebackend.models.Campaign
import org.univesp.natalagapebackend.repositories.CampaignRepository

@Service
class CampaignService(private val campaignRepository: CampaignRepository) {
    fun listAll(): List<Campaign> = campaignRepository.findAll()
    fun findById(id: Long) = campaignRepository.findById(id)

    fun findAllByIsActive() = campaignRepository.findAllByIsActive()

    fun save(campaign: Campaign) = campaignRepository.save(campaign)
    fun update(campaign: Campaign) = campaignRepository.save(campaign)

    @Transactional
    fun delete(id: Long) {
        val campaign = findById(id).orElseThrow { Exception("Campaign not found") }
        campaignRepository.inactivateCampaignById(campaign.campaignId)
    }
}