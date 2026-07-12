package org.univesp.natalagapebackend.controllers

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.univesp.natalagapebackend.models.Sponsor
import org.univesp.natalagapebackend.services.SponsorService

@RestController
@RequestMapping("api/sponsor")
class SponsorController(
    val sponsorService: SponsorService,
) {

    @GetMapping
    fun listAll() : List<Sponsor> = sponsorService.listAll()

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long) : ResponseEntity<Sponsor> {
        return sponsorService.findById(id).map {
            ResponseEntity.ok(it)
        }.orElse(ResponseEntity.notFound().build())
    }

    @PostMapping
    fun save(@RequestBody sponsor: Sponsor) = sponsorService.save(sponsor)

    @PutMapping("/{id}")
    fun update(@PathVariable id: Long, @RequestBody updatedSponsor: Sponsor): ResponseEntity<Sponsor> {
        return sponsorService.findById(id).map { _ ->
            ResponseEntity.ok(sponsorService.update(updatedSponsor))
        }.orElse(ResponseEntity.notFound().build())
    }
}