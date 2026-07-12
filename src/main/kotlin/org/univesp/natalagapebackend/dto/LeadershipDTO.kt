package org.univesp.natalagapebackend.models.DTO

import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.models.Role

data class LeadershipDTO(
    val leaderId: Long,
    val leaderName: String,
    val leaderPhone: String,
    val leaderRole: String,
    val leaderColor: String,
    val userName: String,
    val password: String?
)


fun Leadership.toDTO(): LeadershipDTO {
    return LeadershipDTO(
        leaderId = this.leaderId,
        leaderName = this.leaderName,
        leaderPhone = this.leaderPhone,
        leaderRole = this.leaderRole.toString(),
        leaderColor = this.leaderColor,
        userName = this.userName,
        password = ""
    )
}

fun LeadershipDTO.toEntity(encodedPassword: String): Leadership {

    return Leadership(
        leaderId = this.leaderId,
        leaderName = this.leaderName,
        leaderPhone = this.leaderPhone,
        leaderRole = Role.valueOf(this.leaderRole),
        leaderColor = this.leaderColor,
        userName = this.userName,
        password = encodedPassword
    )
}