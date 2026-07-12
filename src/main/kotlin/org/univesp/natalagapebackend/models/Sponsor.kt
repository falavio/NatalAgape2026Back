package org.univesp.natalagapebackend.models

import jakarta.persistence.*
import lombok.Data

@Data
@Entity
@Table(name = "sponsor")
data class Sponsor (
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val sponsorId: Long,
    @Column(nullable = false)
    val sponsorName: String,
    @Column(nullable = false)
    val sponsorPhone : String,
    @Column(nullable = true)
    val sponsorAddress: String
)
