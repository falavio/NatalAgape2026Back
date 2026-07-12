package org.univesp.natalagapebackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.univesp.natalagapebackend.models.DTO.LeadershipDTO
import org.univesp.natalagapebackend.models.DTO.toDTO
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.services.LeadershipService

@RestController
@RequestMapping("api/leadership")
class LeadershipController(val leadershipService: LeadershipService) {

    @GetMapping
    fun getAllLeaderships(): ResponseEntity<List<LeadershipDTO>> {
        val leaderships = leadershipService.getAllLeaderships()
        return ResponseEntity.ok(leaderships.map { it.toDTO() })
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<LeadershipDTO> {
        return leadershipService.findById(id).map {
            ResponseEntity.ok(it.toDTO())
        }.orElse(ResponseEntity.notFound().build())
    }


    @PostMapping
    fun save(@RequestBody leadershipDTO: LeadershipDTO): ResponseEntity<Leadership> {
        val saved = leadershipService.save(leadershipDTO)
        return ResponseEntity.ok(saved)
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody leadership: LeadershipDTO): ResponseEntity<Leadership> {
        return leadershipService.findById(id).map { _ ->
            ResponseEntity.ok(leadershipService.update(leadership))
        }.orElse(ResponseEntity.notFound().build())
    }
}