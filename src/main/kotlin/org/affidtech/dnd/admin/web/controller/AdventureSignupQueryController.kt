package org.affidtech.dnd.admin.web.controller

import org.affidtech.dnd.admin.service.AdventureSignupService
import org.affidtech.dnd.admin.web.dto.AdventureSignupDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class AdventureSignupQueryController(
	private val adventureSignupService: AdventureSignupService
) {
	@GetMapping("/api/adventures/{adventureId}/signups")
	fun getSignupsForAdventure(@PathVariable adventureId: UUID): List<AdventureSignupDto> =
		adventureSignupService.getAllByAdventure(adventureId)
	
	@GetMapping("/api/users/{userId}/signups")
	fun getSignupsForUser(@PathVariable userId: UUID): List<AdventureSignupDto> =
		adventureSignupService.getAllByUser(userId)
}
