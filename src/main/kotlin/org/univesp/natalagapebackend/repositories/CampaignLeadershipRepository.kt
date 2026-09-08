package org.univesp.natalagapebackend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.univesp.natalagapebackend.models.CampaignLeadership

@Repository
interface CampaignLeadershipRepository : JpaRepository<CampaignLeadership, Long> {

    fun findByCampaignCampaignId(campaignId: Long): List<CampaignLeadership>

    fun findByLeaderLeaderId(leaderId: Long): List<CampaignLeadership>

    fun findByCampaignCampaignIdAndLeaderLeaderId(campaignId: Long, leaderId: Long): CampaignLeadership?
}
