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

}