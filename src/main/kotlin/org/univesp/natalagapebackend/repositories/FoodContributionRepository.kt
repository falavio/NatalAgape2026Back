package org.univesp.natalagapebackend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.univesp.natalagapebackend.models.FoodContribution

@Repository
interface FoodContributionRepository : JpaRepository<FoodContribution, Long> {
    @Query("SELECT fc FROM FoodContribution fc WHERE fc.family.familyId = :id")
    fun findFoodContributionByFamilyId(id: Long): List<FoodContribution>

    @Query("SELECT fc FROM FoodContribution fc WHERE fc.campaign.campaignId = :id")
    fun findFoodContributionByCampaignId(id: Long): List<FoodContribution>
}