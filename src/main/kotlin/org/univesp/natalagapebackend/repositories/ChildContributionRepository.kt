package org.univesp.natalagapebackend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.univesp.natalagapebackend.models.ChildContribution

@Repository
interface ChildContributionRepository : JpaRepository<ChildContribution, Long> {
    @Query("SELECT cc FROM ChildContribution cc WHERE cc.campaign.campaignId = :campaignId")
    fun findChildrenContributionByCampaignId(campaignId: Long): List<ChildContribution>
}