package org.univesp.natalagapebackend.models

import jakarta.persistence.*
import lombok.Data

@Data
@Entity
@Table(name = "leadership")
class Leadership(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val leaderId: Long,
    @Column(nullable = false) var leaderName: String,

    @Column(nullable = false) var leaderPhone: String,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false) var leaderRole: Role,


    @Column(nullable = false) var leaderColor: String,

    @Column(nullable = false, unique = true)
    var userName: String,

    @Column(nullable = true)
    var password: String?

)