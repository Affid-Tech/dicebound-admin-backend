package org.affidtech.dnd.admin.web.controller

import org.affidtech.dnd.admin.service.GameSessionService
import org.affidtech.dnd.admin.web.dto.GameSessionDto
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
class GameSessionQueryController(
	private val gameSessionService: GameSessionService
)  {
	
	@GetMapping("/api/adventures/{adventureId}/sessions")
	fun getSessionsForAdventure(@PathVariable adventureId: UUID): List<GameSessionDto> =
		gameSessionService.getAllByAdventure(adventureId)
	
}