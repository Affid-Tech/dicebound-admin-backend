package org.affidtech.dnd.admin.web.controller

import jakarta.validation.Valid
import org.affidtech.dnd.admin.service.GameSessionService
import org.affidtech.dnd.admin.web.dto.GameSessionCreateDto
import org.affidtech.dnd.admin.web.dto.GameSessionDto
import org.affidtech.dnd.admin.web.dto.GameSessionPatchDto
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/sessions")
class GameSessionController(
	private val gameSessionService: GameSessionService
) {
	@GetMapping
	fun getAll(): List<GameSessionDto> = gameSessionService.getAll()
	
	@GetMapping("/{id}")
	fun getById(@PathVariable id: UUID): GameSessionDto = gameSessionService.getById(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody @Valid dto: GameSessionCreateDto): GameSessionDto =
		gameSessionService.create(dto)
	
	@PatchMapping("/{id}")
	fun patch(@PathVariable id: UUID, @RequestBody dto: GameSessionPatchDto): GameSessionDto =
		gameSessionService.patch(id, dto)
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: UUID) = gameSessionService.delete(id)
	
	@GetMapping("/api/adventures/{adventureId}/sessions")
	fun getSessionsForAdventure(@PathVariable adventureId: UUID): List<GameSessionDto> =
		gameSessionService.getAllByAdventure(adventureId)
	
	@PostMapping("/api/adventures/{adventureId}/sessions")
	@ResponseStatus(HttpStatus.CREATED)
	fun createForAdventure(
		@PathVariable adventureId: UUID,
		@RequestBody @Valid dto: GameSessionCreateDto
	): GameSessionDto {
		// dto.adventureId игнорируем, используем adventureId из path
		val createDto = dto.copy(adventureId = adventureId)
		return gameSessionService.create(createDto)
	}
	
	@DeleteMapping("/api/adventures/{adventureId}/sessions/{sessionId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun deleteSessionFromAdventure(
		@PathVariable adventureId: UUID,
		@PathVariable sessionId: UUID
	) {
		// Можно добавить проверку, что session действительно принадлежит adventure
		gameSessionService.delete(sessionId)
	}
}