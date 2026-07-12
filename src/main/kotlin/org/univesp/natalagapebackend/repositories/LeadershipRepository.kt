package org.univesp.natalagapebackend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.univesp.natalagapebackend.models.Leadership

@Repository
interface LeadershipRepository : JpaRepository<Leadership, Long>, LeadershipCustomRepository {
    fun findByUserName(userName: String): Leadership?
}
