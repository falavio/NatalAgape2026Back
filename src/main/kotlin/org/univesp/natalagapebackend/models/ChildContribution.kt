package org.univesp.natalagapebackend.models

import jakarta.persistence.*
import lombok.Data
import java.time.LocalDate

@Data
@Entity
@Table(name = "child_contribution")
data class ChildContribution(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    @ManyToOne
    //@JoinColumn(name = "campaign_id", nullable = false)
    val campaign: Campaign,
    @ManyToOne
    //@JoinColumn(name = "sponsor_id", nullable = false)
    val sponsor: Sponsor,
    @ManyToOne
    val leadership: Leadership,
    @ManyToOne
    //@JoinColumn(name = "child_id", nullable = false)
    val child: Child,
    val wasDelivered: Boolean? = null,
    val toyDelivered: Boolean? = null,
    val acceptance: LocalDate? = null,
    val observation: String? = null
)