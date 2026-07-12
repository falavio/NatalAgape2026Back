package org.univesp.natalagapebackend.controllers

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.springframework.http.ResponseEntity
import org.univesp.natalagapebackend.models.Color
import org.univesp.natalagapebackend.models.DTO.LeadershipDTO
import org.univesp.natalagapebackend.models.DTO.toDTO
import org.univesp.natalagapebackend.models.Leadership
import org.univesp.natalagapebackend.models.Role
import org.univesp.natalagapebackend.services.LeadershipService
import java.util.*

class LeadershipControllerTest {

    private lateinit var leadershipService: LeadershipService
    private lateinit var leadershipController: LeadershipController

    @BeforeEach
    fun setUp() {
        leadershipService = mock(LeadershipService::class.java)
        leadershipController = LeadershipController(leadershipService)
    }

    @Test
    fun `getAllLeaderships deve retornar lista de liderancas`() {
        val leaderships = listOf(
            Leadership(1L, "Leader1", "123456789", Role.ADMIN, Color.RED.toString(), "username1", "password1"),
            Leadership(2L, "Leader2", "987654321", Role.LEADER, Color.BLUE.toString(), "username2", "password2")
        )
        `when`(leadershipService.getAllLeaderships()).thenReturn(leaderships)

        val result = leadershipController.getAllLeaderships()

        assertEquals(ResponseEntity.ok(leaderships.map { it.toDTO() }), result)
    }

    @Test
    fun `getAllLeaderships deve retornar lista vazia`() {
        `when`(leadershipService.getAllLeaderships()).thenReturn(emptyList())

        val result = leadershipController.getAllLeaderships()

        assertEquals(ResponseEntity.ok(emptyList<LeadershipDTO>()), result)
    }

    @Test
    fun `findById deve retornar lideranca existente`() {
        val leadership = Leadership(1L, "Leader1", "123456789", Role.ADMIN, Color.RED.toString(), "username", "password")
        `when`(leadershipService.findById(1L)).thenReturn(Optional.of(leadership))

        val result = leadershipController.findById(1L)

        assertEquals(ResponseEntity.ok(leadership.toDTO()), result)
    }

    @Test
    fun `findById deve retornar 404 para ID inexistente`() {
        `when`(leadershipService.findById(999L)).thenReturn(Optional.empty())

        val result = leadershipController.findById(999L)

        assertEquals(ResponseEntity.notFound().build<LeadershipDTO>(), result)
    }

    @Test
    fun `save deve criar nova lideranca`() {
        val leadershipDTO = LeadershipDTO(1L, "Leader1", "123456789", Role.ADMIN.toString(), Color.RED.toString(), "username", "password")
        val leadership = Leadership(1L, "Leader1", "123456789", Role.ADMIN, Color.RED.toString(), "username", "password")
        `when`(leadershipService.save(leadershipDTO)).thenReturn(leadership)

        val result = leadershipController.save(leadershipDTO)

        assertEquals(ResponseEntity.ok(leadership), result)
    }

    @Test
    fun `update deve modificar lideranca existente`() {
        val leadershipDTO = LeadershipDTO(1L, "Leader1", "123456789", Role.ADMIN.toString(), Color.RED.toString(), "username", "password")
        val leadership = Leadership(1L, "Leader1", "123456789", Role.ADMIN, Color.RED.toString(), "username", "password")
        `when`(leadershipService.findById(1L)).thenReturn(Optional.of(leadership))
        `when`(leadershipService.update(leadershipDTO)).thenReturn(leadership)

        val result = leadershipController.update(1L, leadershipDTO)

        assertEquals(ResponseEntity.ok(leadership), result)
    }

    @Test
    fun `update deve retornar 404 para lideranca inexistente`() {
        val leadershipDTO = LeadershipDTO(1L, "Leader1", "123456789", Role.ADMIN.toString(), Color.RED.toString(), "username", "password")
        `when`(leadershipService.findById(999L)).thenReturn(Optional.empty())

        val result = leadershipController.update(999L, leadershipDTO)

        assertEquals(ResponseEntity.notFound().build<Leadership>(), result)
    }
}