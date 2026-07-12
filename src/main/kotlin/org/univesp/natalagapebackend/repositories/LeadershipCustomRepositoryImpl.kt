package org.univesp.natalagapebackend.repositories

import jakarta.persistence.EntityManager
import jakarta.transaction.Transactional
import org.springframework.stereotype.Repository
import org.univesp.natalagapebackend.models.Leadership

@Repository
class LeadershipCustomRepositoryImpl(
    private val entityManager: EntityManager
) : LeadershipCustomRepository {

    @Transactional
    override fun updateLeadership(leadership: Leadership): Leadership {
        var existingLeadership = entityManager.find(Leadership::class.java, leadership.leaderId)
            ?: throw IllegalArgumentException("Leadership com ID ${leadership.leaderId} não encontrado.")

        existingLeadership.leaderName = leadership.leaderName
        existingLeadership.leaderPhone = leadership.leaderPhone
        existingLeadership.leaderRole = leadership.leaderRole
        existingLeadership.leaderColor = leadership.leaderColor
        existingLeadership.userName = leadership.userName

        if (!leadership.password.isNullOrEmpty() && leadership.password != existingLeadership.password) {
            existingLeadership.password = leadership.password
        }

        entityManager.merge(existingLeadership)
        return existingLeadership
    }
}