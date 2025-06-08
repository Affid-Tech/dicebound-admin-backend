package org.affidtech.dnd.admin.service

import org.affidtech.dnd.admin.domain.AdventureSignupStatus
import org.affidtech.dnd.admin.exception.AlreadyExistsException
import org.affidtech.dnd.admin.exception.NotFoundException
import org.affidtech.dnd.admin.repo.AdventureRepository
import org.affidtech.dnd.admin.repo.AdventureSignupRepository
import org.affidtech.dnd.admin.repo.UserRepository
import org.affidtech.dnd.admin.web.AdventureSignupMapper
import org.affidtech.dnd.admin.web.dto.AdventureSignupCreateDto
import org.affidtech.dnd.admin.web.dto.AdventureSignupDto
import org.affidtech.dnd.admin.web.dto.AdventureSignupPatchDto
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

private const val SIGNUP_NOT_FOUND = "Signup not found"

@Service
class AdventureSignupService(
	private val adventureSignupRepository: AdventureSignupRepository,
	private val adventureRepository: AdventureRepository,
	private val userRepository: UserRepository,
	private val adventureSignupMapper: AdventureSignupMapper
) {
	
	fun getAll(): List<AdventureSignupDto> =
		adventureSignupRepository.findAll().map(adventureSignupMapper::toDto)
	
	fun getById(id: UUID): AdventureSignupDto =
		adventureSignupRepository.findById(id)
			.map(adventureSignupMapper::toDto)
			.orElseThrow { NotFoundException(SIGNUP_NOT_FOUND) }
	
	fun getAllByAdventure(adventureId: UUID): List<AdventureSignupDto> =
		adventureSignupRepository.findAllByAdventureId(adventureId).map(adventureSignupMapper::toDto)
	
	fun getAllByUser(userId: UUID): List<AdventureSignupDto> =
		adventureSignupRepository.findAllByUserId(userId).map(adventureSignupMapper::toDto)
	
	@Transactional
	fun create(dto: AdventureSignupCreateDto): AdventureSignupDto {
		if (adventureSignupRepository.existsByAdventureIdAndUserId(dto.adventureId, dto.userId))
			throw AlreadyExistsException("Signup already exists")
		val adventure = adventureRepository.findById(dto.adventureId)
			.orElseThrow { NotFoundException("Adventure not found") }
		val user = userRepository.findById(dto.userId)
			.orElseThrow { NotFoundException("User not found") }
		val entity = adventureSignupMapper.toEntity(dto, adventure, user).copy(id = UUID.randomUUID(), status = AdventureSignupStatus.PENDING)
		val saved = adventureSignupRepository.save(entity)
		return adventureSignupMapper.toDto(saved)
	}
	
	@Transactional
	fun patch(id: UUID, dto: AdventureSignupPatchDto): AdventureSignupDto {
		val signup = adventureSignupRepository.findById(id)
			.orElseThrow { NotFoundException(SIGNUP_NOT_FOUND) }
		adventureSignupMapper.updateEntityFromPatch(dto, signup)
		val saved = adventureSignupRepository.save(signup)
		return adventureSignupMapper.toDto(saved)
	}
	
	@Transactional
	fun delete(id: UUID) {
		if (!adventureSignupRepository.existsById(id)) throw NotFoundException(SIGNUP_NOT_FOUND)
		adventureSignupRepository.deleteById(id)
	}
}