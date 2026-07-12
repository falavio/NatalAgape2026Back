package org.univesp.natalagapebackend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import org.univesp.natalagapebackend.models.Sponsor

@Repository
interface SponsorRepository : JpaRepository<Sponsor, Long> {
}