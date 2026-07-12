package org.univesp.natalagapebackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.univesp.natalagapebackend.dto.FoodContributionEditResponse
import org.univesp.natalagapebackend.dto.FoodContributionReport
import org.univesp.natalagapebackend.dto.FoodContributionRequest
import org.univesp.natalagapebackend.dto.FoodContributionResponse
import org.univesp.natalagapebackend.dto.toDTOEditResponse
import org.univesp.natalagapebackend.dto.toDTOResponse
import org.univesp.natalagapebackend.models.FoodContribution
import org.univesp.natalagapebackend.services.FoodContributionService

@Controller
@RequestMapping("api/food-contribution")
class FoodContributionController(val foodContributionService: FoodContributionService) {

    @GetMapping
    fun listAll(): ResponseEntity<List<FoodContributionResponse>> {
        return ResponseEntity.ok(foodContributionService.listAll()
            .map { foodContribution -> toDTOResponse(foodContribution) }
        )
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<FoodContributionEditResponse> {
        return foodContributionService.findById(id).map { foodContribution ->
            ResponseEntity.ok(toDTOEditResponse(foodContribution))
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun save(@RequestBody foodContribution: FoodContributionRequest): ResponseEntity<FoodContribution> {
        return ResponseEntity.ok(foodContributionService.save(foodContribution))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody foodContribution: FoodContributionRequest): ResponseEntity<FoodContribution> {
        return foodContributionService.findById(id)
            .map { existingFoodContribution ->
                val foodContributionToUpdate = foodContribution.copy(id = existingFoodContribution.id)
                val updatedEntity = foodContributionService.update(foodContributionToUpdate)
                ResponseEntity.ok(updatedEntity)
            }
            .orElse(ResponseEntity.notFound().build())
    }

    @GetMapping("report/{campaignId}")
    fun report(@PathVariable campaignId: Long): ResponseEntity<FoodContributionReport> {
        return ResponseEntity.ok(foodContributionService.report(campaignId))
    }
}