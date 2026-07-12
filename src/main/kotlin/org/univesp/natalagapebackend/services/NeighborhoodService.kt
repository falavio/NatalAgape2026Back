package org.univesp.natalagapebackend.services

import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import org.univesp.natalagapebackend.models.Neighborhood
import org.univesp.natalagapebackend.repositories.NeighborhoodRepository

@Service
class NeighborhoodService(private val neighborhoodRepository: NeighborhoodRepository) {
    fun listAll(): List<Neighborhood> = neighborhoodRepository.findAll(Sort.by("neighborhoodName"))
    fun findById(id: Long) = neighborhoodRepository.findById(id)
    fun save(neighborhood: Neighborhood) = neighborhoodRepository.save(neighborhood)
    fun update(neighborhood: Neighborhood) = neighborhoodRepository.save(neighborhood)
}