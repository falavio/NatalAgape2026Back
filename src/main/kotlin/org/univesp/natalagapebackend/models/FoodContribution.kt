package org.univesp.natalagapebackend.models

import jakarta.persistence.*
import lombok.Data
import java.time.LocalDate

@Data
@Entity
@Table(name = "food_contribution")
data class FoodContribution(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @ManyToOne
    val campaign: Campaign,
    @ManyToOne
    val leader: Leadership,
    @ManyToOne
    val sponsor: Sponsor,
    @ManyToOne
    val family: Family,
    val wasDelivered: Boolean? = null,
    val paidInSpecies: Boolean? = null,
    val donationDate: LocalDate? = null,
    val observation: String? = null
)
