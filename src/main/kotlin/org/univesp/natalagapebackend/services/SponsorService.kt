package org.univesp.natalagapebackend.services

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.univesp.natalagapebackend.models.Sponsor
import org.univesp.natalagapebackend.repositories.SponsorRepository

@Service
class SponsorService(private val sponsorRepository: SponsorRepository) {
    fun listAll(): List<Sponsor> = sponsorRepository.findAll(Sort.by("sponsorName"))
    fun findById(id: Long) = sponsorRepository.findById(id)
    fun save(sponsor: Sponsor) = sponsorRepository.save(sponsor)
    fun update(sponsor: Sponsor) = sponsorRepository.save(sponsor)
}