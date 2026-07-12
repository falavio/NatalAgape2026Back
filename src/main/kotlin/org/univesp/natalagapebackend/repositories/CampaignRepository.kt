package org.univesp.natalagapebackend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.univesp.natalagapebackend.models.Campaign

@Repository
interface CampaignRepository : JpaRepository<Campaign, Long>
{
    @Query("SELECT c FROM Campaign c WHERE c.isActive = true")
    fun findAllByIsActive(): List<Campaign>

    @Modifying
    @Query("UPDATE Campaign c SET c.isActive = false WHERE c.campaignId = :id")
    fun inactivateCampaignById(id: Long)
}