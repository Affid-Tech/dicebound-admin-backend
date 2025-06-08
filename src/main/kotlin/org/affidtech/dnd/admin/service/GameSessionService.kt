package org.affidtech.dnd.admin.service

import org.affidtech.dnd.admin.exception.NotFoundException
import org.affidtech.dnd.admin.repo.AdventureRepository
import org.affidtech.dnd.admin.repo.GameSessionRepository
import org.affidtech.dnd.admin.web.GameSessionMapper
import org.affidtech.dnd.admin.web.dto.GameSessionCreateDto
import org.affidtech.dnd.admin.web.dto.GameSessionDto
import org.affidtech.dnd.admin.web.dto.GameSessionPatchDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

private const val SESSION_NOT_FOUND = "Session not found"

@Service
class GameSessionService(
	private val gameSessionRepository: GameSessionRepository,
	private val adventureRepository: AdventureRepository,
	private val gameSessionMapper: GameSessionMapper
) {
	
	fun getAll(): List<GameSessionDto> =
		gameSessionRepository.findAll().map(gameSessionMapper::toDto)
	
	fun getById(id: UUID): GameSessionDto =
		gameSessionRepository.findById(id)
			.map(gameSessionMapper::toDto)
			.orElseThrow { NotFoundException(SESSION_NOT_FOUND) }
	
	fun getAllByAdventure(adventureId: UUID): List<GameSessionDto> =
		gameSessionRepository.findAllByAdventureId(adventureId).map(gameSessionMapper::toDto)
	
	@Transactional
	fun create(dto: GameSessionCreateDto): GameSessionDto {
		val adventure = adventureRepository.findById(dto.adventureId)
			.orElseThrow { NotFoundException("Adventure not found") }
		val entity = gameSessionMapper.toEntity(dto, adventure)
		val saved = gameSessionRepository.save(entity)
		return gameSessionMapper.toDto(saved)
	}
	
	@Transactional
	fun patch(id: UUID, dto: GameSessionPatchDto): GameSessionDto {
		val session = gameSessionRepository.findById(id)
			.orElseThrow { NotFoundException(SESSION_NOT_FOUND) }
		gameSessionMapper.updateEntityFromPatch(dto, session)
		val saved = gameSessionRepository.save(session)
		return gameSessionMapper.toDto(saved)
	}
	
	@Transactional
	fun delete(id: UUID) {
		if (!gameSessionRepository.existsById(id)) throw NotFoundException(SESSION_NOT_FOUND)
		gameSessionRepository.deleteById(id)
	}
}