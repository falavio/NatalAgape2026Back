package org.univesp.natalagapebackend.repositories

import org.univesp.natalagapebackend.models.Leadership

interface LeadershipCustomRepository {
    fun updateLeadership(leadership: Leadership): Leadership
}