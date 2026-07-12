package org.univesp.natalagapebackend.models

import jakarta.persistence.*
import lombok.Data

@Data
@Entity
@Table(name = "neighborhood")
data class Neighborhood(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val neighborhoodId: Long,
    @Column(nullable = false) val neighborhoodName: String,
)
