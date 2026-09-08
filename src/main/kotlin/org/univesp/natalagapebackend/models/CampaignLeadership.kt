package org.univesp.natalagapebackend.models

import jakarta.persistence.*
import lombok.Data

@Data
@Entity
@Table(name = "campaign_leadership")
data class CampaignLeadership(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long = 0,

    @ManyToOne
    @JoinColumn(name = "campaign_id", nullable = false)
    val campaign: Campaign,

    @ManyToOne
    @JoinColumn(name = "leader_id", nullable = false)
    val leader: Leadership,

    @Column(name = "leader_color", nullable = false)
    val leaderColor: String
)
