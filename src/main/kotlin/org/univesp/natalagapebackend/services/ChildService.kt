package org.univesp.natalagapebackend.services

import jakarta.transaction.Transactional
import org.springframework.stereotype.Service
import org.univesp.natalagapebackend.dto.ChildRequest
import org.univesp.natalagapebackend.dto.toEntity
import org.univesp.natalagapebackend.models.Child
import org.univesp.natalagapebackend.repositories.ChildRepository
import java.util.*
import kotlin.jvm.optionals.getOrElse

@Service
class ChildService(private val childRepository: ChildRepository, private val familyService: FamilyService) {

    fun listAll(): List<Child> = childRepository.findAllActive()

    fun findById(id: Long): Optional<Child> = childRepository.findById(id)

    fun findByFamilyId(familyId: Long): List<Child>? {
       return childRepository.findAllByFamilyId(familyId)
    }


    fun save(childRequest: ChildRequest): Child {
        val family = familyService.findById(childRequest.familyId).getOrElse {
            throw IllegalArgumentException("Family not found")
        }
        return childRepository.save(childRequest.toEntity(family))
    }

    fun update(childRequest: ChildRequest): Child {
        return childRepository.findById(childRequest.childId).map { existingChild ->
            val family = familyService.findById(childRequest.familyId).getOrElse {
                throw IllegalArgumentException("Family not found")
            }
            val childToUpdate = Child(
                childId = existingChild.childId,
                childName = childRequest.childName,
                birthDate = ChildRequest.parseBirthDate(childRequest.birthDate),
                gender = childRequest.gender,
                clothes = childRequest.clothes,
                shoes = childRequest.shoes,
                family = family,
                pictureUrl = childRequest.pictureUrl
            )
            childRepository.save(childToUpdate)
        }.orElseThrow { IllegalArgumentException("Child not found") }
    }

    @Transactional
    fun deleteById(id: Long) = childRepository.deactivateChild(id)

    @Transactional
    fun deleteByFamilyId(familyId: Long) {
        val children = childRepository.findAllByFamilyId(familyId)
        children?.forEach { child ->
            childRepository.deactivateChild(child.childId)
        }
    }
}