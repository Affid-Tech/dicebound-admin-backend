package org.affidtech.dnd.admin.web.controller

import org.affidtech.dnd.admin.service.UserService
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/api/users")
class UserRoleController(
	private val userService: UserService
) {
	
	@PostMapping("/{id}/roles/player")
	fun addPlayer(@PathVariable id: UUID) = userService.addPlayerProfile(id)
	
	@DeleteMapping("/{id}/roles/player")
	fun removePlayer(@PathVariable id: UUID) = userService.removePlayerProfile(id)
	
	@PostMapping("/{id}/roles/dungeon-master")
	fun addDungeonMaster(@PathVariable id: UUID) = userService.addDungeonMasterProfile(id)
	
	@DeleteMapping("/{id}/roles/dungeon-master")
	fun removeDungeonMaster(@PathVariable id: UUID) = userService.removeDungeonMasterProfile(id)
	
	@PostMapping("/{id}/roles/admin")
	fun addAdmin(@PathVariable id: UUID) = userService.addAdminProfile(id)
	
	@DeleteMapping("/{id}/roles/admin")
	fun removeAdmin(@PathVariable id: UUID) = userService.removeAdminProfile(id)
}
