package org.univesp.natalagapebackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.univesp.natalagapebackend.dto.ChildContributionEditResponse
import org.univesp.natalagapebackend.dto.ChildContributionReport
import org.univesp.natalagapebackend.dto.ChildContributionRequest
import org.univesp.natalagapebackend.dto.ChildContributionResponse
import org.univesp.natalagapebackend.dto.toDTOEditResponse
import org.univesp.natalagapebackend.dto.toDTOResponse
import org.univesp.natalagapebackend.models.ChildContribution
import org.univesp.natalagapebackend.services.ChildContributionService

@RestController
@RequestMapping("api/child-contribution")
class ChildContributionController(private val childContributionService: ChildContributionService) {

    @GetMapping
    fun listAll(): ResponseEntity<List<ChildContributionResponse>> {
        return ResponseEntity.ok(
            childContributionService.listAll().map { toDTOResponse(it) }
        )
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<ChildContributionEditResponse> {
        return childContributionService.findById(id).map {
            ResponseEntity.ok(toDTOEditResponse(it))
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun save(@RequestBody request: ChildContributionRequest): ResponseEntity<ChildContribution> {
        return ResponseEntity.ok(childContributionService.save(request))
    }

    @PutMapping("/{id}")
    fun update(
        @PathVariable id: Long,
        @RequestBody request: ChildContributionRequest
    ): ResponseEntity<ChildContribution> {
        return childContributionService.findById(id).map { existing ->
            val updatedEntity = childContributionService.update(id, request)
            ResponseEntity.ok(updatedEntity)
        }.orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("/report/{campaignId}")
    fun report(@PathVariable campaignId: Long): ResponseEntity<ChildContributionReport> {
        return ResponseEntity.ok(childContributionService.report(campaignId))
    }
}