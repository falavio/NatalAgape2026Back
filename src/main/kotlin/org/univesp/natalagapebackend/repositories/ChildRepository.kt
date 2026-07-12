package org.univesp.natalagapebackend.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.univesp.natalagapebackend.models.Child

@Repository
interface ChildRepository : JpaRepository<Child, Long> {
    @Query("select * from child where family_id = :familyId and is_active = true order by child_name", nativeQuery = true)
    fun findAllByFamilyId(familyId: Long): List<Child>?

    @Query("select * from child where is_active = true order by child_name", nativeQuery = true)
    fun findAllActive(): List<Child>

    @Modifying
    @Query("update child set is_active = false where child_id = :id", nativeQuery = true)
    fun deactivateChild(id: Long)
}