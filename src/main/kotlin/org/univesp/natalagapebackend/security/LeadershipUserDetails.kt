package org.univesp.natalagapebackend.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.univesp.natalagapebackend.models.Leadership

class LeadershipUserDetails(
    private val leadership: Leadership
) : UserDetails {
    override fun getAuthorities(): Collection<GrantedAuthority> =
        listOf(SimpleGrantedAuthority("ROLE_${leadership.leaderRole.name}"))

    override fun getPassword() = leadership.password
    override fun getUsername() = leadership.userName
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true
    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
}