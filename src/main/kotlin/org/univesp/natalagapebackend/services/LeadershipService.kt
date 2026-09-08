package org.univesp.natalagapebackend.services

import org.springframework.data.domain.Sort
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.univesp.natalagapebackend.models.DTO.LeadershipDTO
import org.univesp.natalagapebackend.models.DTO.toEntity
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.repositories.CampaignLeadershipRepository
import org.univesp.natalagapebackend.repositories.LeadershipRepository

@Service
class LeadershipService(
    private val leadershipRepository: LeadershipRepository,
    private val passwordEncoder: PasswordEncoder,
    private val campaignLeadershipRepository: CampaignLeadershipRepository
) {
    fun getAllLeaderships(): List<Leadership> = leadershipRepository.findAll(Sort.by("leaderName"))

    fun listByCampaignId(campaignId: Long): List<Leadership> =
        campaignLeadershipRepository.findByCampaignCampaignId(campaignId)
            .map { it.leader }
            .sortedBy { it.leaderName }

    fun findById(id: Long) = leadershipRepository.findById(id)

    fun save(leadership: Leadership): Leadership = leadershipRepository.save(leadership)

    fun update(leadership: Leadership): Leadership = leadershipRepository.save(leadership)

    fun update(leadershipNew: LeadershipDTO): Leadership {
        var hashedPassword = ""
        if (!leadershipNew.password.isNullOrEmpty()) {
            hashedPassword = passwordEncoder.encode(leadershipNew.password)
        }

        return leadershipRepository.updateLeadership(leadershipNew.toEntity(hashedPassword))
    }

    fun save(leadership: LeadershipDTO): Leadership {

        val hashedPassword = passwordEncoder.encode(leadership.password)

        return leadershipRepository.save(leadership.toEntity(hashedPassword))
    }
}
