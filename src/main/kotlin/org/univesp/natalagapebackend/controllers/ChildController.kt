package org.univesp.natalagapebackend.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.univesp.natalagapebackend.dto.ChildRequest
import org.univesp.natalagapebackend.dto.ChildResponse
import org.univesp.natalagapebackend.dto.toResponse
import org.univesp.natalagapebackend.models.Child
import org.univesp.natalagapebackend.services.ChildService
import org.univesp.natalagapebackend.services.FamilyService

@RestController
@RequestMapping("api/child")
class ChildController(
    val childService: ChildService,
    val familyService: FamilyService
) {

    @GetMapping
    fun listAll(): List<Child> = childService.listAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Child> {
        return childService.findById(id).map { child ->
            ResponseEntity.ok(child)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun save(@RequestBody childRequest: ChildRequest): ResponseEntity<ChildResponse> {
        val family = familyService.findById(childRequest.familyId)
            .orElseThrow { RuntimeException("Family not found") }
        return ResponseEntity.ok(childService.save(childRequest).toResponse(family))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody updatedChild: ChildRequest): ResponseEntity<ChildResponse> {

        return childService.findById(id)
            .map { existingChild ->
                val childToUpdate = updatedChild.copy(childId = existingChild.childId)

                val updatedEntity = childService.update(childToUpdate)

                val family = familyService.findById(updatedChild.familyId)
                    .orElseThrow { RuntimeException("Family not found") }


                ResponseEntity.ok(updatedEntity.toResponse(family))

            }
            .orElse(ResponseEntity.notFound().build())
    }

    @DeleteMapping("/{id}")
    fun deleteById(@PathVariable id: Long): ResponseEntity<Void> {
        return childService.findById(id).map { child ->
            childService.deleteById(child.childId)
            ResponseEntity<Void>(HttpStatus.OK)
        }.orElse(ResponseEntity.notFound().build())
    }
}