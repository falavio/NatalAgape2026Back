package org.univesp.natalagapebackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.univesp.natalagapebackend.models.Neighborhood
import org.univesp.natalagapebackend.services.NeighborhoodService

@RestController
@RequestMapping("api/neighborhood")
class NeighborhoodController(private val neighborhoodService: NeighborhoodService) {
    @GetMapping
    fun listAll() = neighborhoodService.listAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<Neighborhood> {
        return neighborhoodService.findById(id).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun save(@RequestBody neighborhood: Neighborhood) = neighborhoodService.save(neighborhood)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody neighborhood: Neighborhood) : ResponseEntity<Neighborhood> {
        return neighborhoodService.findById(id).map { _ ->
            ResponseEntity.ok(neighborhoodService.update(neighborhood))
        }.orElse(ResponseEntity.notFound().build())
    }
}