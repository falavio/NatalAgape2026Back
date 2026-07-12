package org.univesp.natalagapebackend.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.univesp.natalagapebackend.dto.FamilyDTOInput

import org.univesp.natalagapebackend.dto.FamilyWithChildrenDTO
import org.univesp.natalagapebackend.dto.toDTOOutput
import org.univesp.natalagapebackend.models.DTO.FamilyDTOOutput
import org.univesp.natalagapebackend.models.DTO.toDTOOutput
import org.univesp.natalagapebackend.services.ChildService
import org.univesp.natalagapebackend.services.FamilyService

@RestController
@RequestMapping("api/family")
class FamilyController(
    val familyService: FamilyService,
    val childService: ChildService
) {

    @GetMapping
    fun listAll(): List<FamilyWithChildrenDTO> {
        return familyService.listAll().map { family ->
            val children = childService.findByFamilyId(family.familyId)
            toDTOOutput(family, children)
        }
    }

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<FamilyWithChildrenDTO>? {
        return familyService.findById(id).map { family ->
            val children = childService.findByFamilyId(family.familyId)
            toDTOOutput(family, children) }
            .map { ResponseEntity.ok(it) }
            .orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun save(@RequestBody family: FamilyDTOInput): ResponseEntity<FamilyDTOOutput> {
        return ResponseEntity.ok(familyService.save(family).toDTOOutput())
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody updatedFamily: FamilyDTOInput): ResponseEntity<FamilyDTOOutput> {
        return familyService.findById(id)
            .map { existingFamily ->
                val familyToUpdate = updatedFamily.copy(familyId = existingFamily.familyId)
                val updatedEntity = familyService.update(familyToUpdate)
                ResponseEntity.ok(updatedEntity.toDTOOutput())
            }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteFamily(@PathVariable id: Long): ResponseEntity<Void> {
        return if (familyService.findById(id).isPresent) {
            childService.deleteByFamilyId(id)
            familyService.deleteFamily(id)
            ResponseEntity<Void>(HttpStatus.OK)
        } else {
            ResponseEntity.notFound().build()
        }
    }
}