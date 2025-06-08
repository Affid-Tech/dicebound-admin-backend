package org.affidtech.dnd.admin.service

import org.affidtech.dnd.admin.exception.NotFoundException
import org.affidtech.dnd.admin.repo.AdventureRepository
import org.affidtech.dnd.admin.repo.UserRepository
import org.affidtech.dnd.admin.web.AdventureMapper
import org.affidtech.dnd.admin.web.dto.AdventureCreateDto
import org.affidtech.dnd.admin.web.dto.AdventureDto
import org.affidtech.dnd.admin.web.dto.AdventurePatchDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

private const val ADVENTURE_NOT_FOUND = "Adventure not found"

private const val DUNGEON_MASTER_NOT_FOUND = "Dungeon master not found"

@Service
class AdventureService(
	private val adventureRepository: AdventureRepository,
	private val userRepository: UserRepository,
	private val adventureMapper: AdventureMapper
) {
	
	fun getAll(): List<AdventureDto> =
		adventureRepository.findAll().map(adventureMapper::toDto)
	
	fun getById(id: UUID): AdventureDto =
		adventureRepository.findById(id)
			.map(adventureMapper::toDto)
			.orElseThrow { NotFoundException(ADVENTURE_NOT_FOUND) }
	
	@Transactional
	fun create(dto: AdventureCreateDto): AdventureDto {
		val dm = userRepository.findById(dto.dungeonMasterId)
			.orElseThrow { NotFoundException(DUNGEON_MASTER_NOT_FOUND) }
		val entity = adventureMapper.toEntity(dto, dm).copy(id = UUID.randomUUID())
		val saved = adventureRepository.save(entity)
		return adventureMapper.toDto(saved)
	}
	
	@Transactional
	fun patch(id: UUID, dto: AdventurePatchDto): AdventureDto {
		val adventure = adventureRepository.findById(id)
			.orElseThrow { NotFoundException(ADVENTURE_NOT_FOUND) }
		
		val dm = dto.dungeonMasterId?.let {
			userRepository.findById(it)
				.orElseThrow { NotFoundException(DUNGEON_MASTER_NOT_FOUND) }
		}
		
		adventureMapper.updateEntityFromPatch(dto, adventure, dm)
		val saved = adventureRepository.save(adventure)
		return adventureMapper.toDto(saved)
	}
	
	@Transactional
	fun delete(id: UUID) {
		if (!adventureRepository.existsById(id)) throw NotFoundException(ADVENTURE_NOT_FOUND)
		adventureRepository.deleteById(id)
	}
}
