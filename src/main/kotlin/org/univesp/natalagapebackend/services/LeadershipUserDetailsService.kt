package org.univesp.natalagapebackend.services

import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.univesp.natalagapebackend.repositories.LeadershipRepository
import org.univesp.natalagapebackend.security.LeadershipUserDetails

@Service
class LeadershipUserDetailsService(
    private val leadershipRepository: LeadershipRepository
) : UserDetailsService {
    override fun loadUserByUsername(userName: String): UserDetails {
        val leader = leadershipRepository.findByUserName(userName)
            ?: throw UsernameNotFoundException("User not found")
        return LeadershipUserDetails(leader)
    }
}