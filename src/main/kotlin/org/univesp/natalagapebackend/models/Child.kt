package org.univesp.natalagapebackend.models

import jakarta.persistence.*
import lombok.Data
import java.time.LocalDate

@Data
@Entity
@Table(name = "child")
data class Child(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val childId: Long = 0,

    @Column(nullable = false)
    val childName: String,

    @Column(nullable = false)
    val gender: String,

    @Column(nullable = false)
    val birthDate: LocalDate,

    val clothes: String? = null,

    val shoes: String? = null,

    val pictureUrl: String? = null,

    @Column(nullable = false)
    var isActive: Boolean = true,

    @ManyToOne
    @JoinColumn(name = "family_id")
    var family: Family
)