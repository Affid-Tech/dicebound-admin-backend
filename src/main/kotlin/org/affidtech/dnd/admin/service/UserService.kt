package org.affidtech.dnd.admin.service

import org.affidtech.dnd.admin.domain.AdminProfileEntity
import org.affidtech.dnd.admin.domain.DungeonMasterProfileEntity
import org.affidtech.dnd.admin.domain.PlayerProfileEntity
import org.affidtech.dnd.admin.domain.UserEntity
import org.affidtech.dnd.admin.exception.AlreadyExistsException
import org.affidtech.dnd.admin.exception.NotFoundException
import org.affidtech.dnd.admin.repo.AdminProfileRepository
import org.affidtech.dnd.admin.repo.DungeonMasterProfileRepository
import org.affidtech.dnd.admin.repo.PlayerProfileRepository
import org.affidtech.dnd.admin.repo.UserRepository
import org.affidtech.dnd.admin.web.UserMapper
import org.affidtech.dnd.admin.web.dto.PageResponseDto
import org.affidtech.dnd.admin.web.dto.PlayerCreateDto
import org.affidtech.dnd.admin.web.dto.UserCreateDto
import org.affidtech.dnd.admin.web.dto.UserDto
import org.affidtech.dnd.admin.web.dto.UserPatchDto
import org.affidtech.dnd.admin.web.dto.UserRole
import org.affidtech.dnd.admin.web.dto.toPageResponseDto
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

private const val USER_NOT_FOUND = "User not found"

@Service
@Transactional(readOnly = true)
class UserService(
	private val userRepository: UserRepository,
	private val userMapper: UserMapper,
	private val playerProfileRepository: PlayerProfileRepository,
	private val dungeonMasterProfileRepository: DungeonMasterProfileRepository,
	private val adminProfileRepository: AdminProfileRepository
) {
	
	fun getAll(): List<UserDto> =
		userRepository.findAll().map { user -> toUserDtoWithRoles(user) }
	
	fun getById(id: UUID): UserDto =
		userRepository.findById(id)
			.map { user -> toUserDtoWithRoles(user) }
			.orElseThrow { NotFoundException(USER_NOT_FOUND) }
	
	fun search(pageable: Pageable, role: UserRole?, keyword: String?): PageResponseDto<UserDto> {
		val page = when (role) {
			null -> {
				if (keyword.isNullOrBlank()) {
					userRepository.findAll(pageable)
				} else {
					userRepository.search(keyword, pageable)
				}
			}
			UserRole.PLAYER -> playerProfileRepository.searchUsers(keyword, pageable)
			UserRole.DUNGEON_MASTER -> dungeonMasterProfileRepository.findUsers( pageable)
			UserRole.ADMIN -> adminProfileRepository.findUsers(pageable)
		}
		
		return page
			.map { user -> toUserDtoWithRoles(user) }
			.toPageResponseDto()
	}
	
	fun toUserDtoWithRoles(user: UserEntity): UserDto =
		userMapper.toDto(user)
			.copy(
				roles = buildList {
					if (playerProfileRepository.existsByUser(user)) add(UserRole.PLAYER)
					if (dungeonMasterProfileRepository.existsByUser(user)) add(UserRole.DUNGEON_MASTER)
					if (adminProfileRepository.existsByUser(user)) add(UserRole.ADMIN)
				}
			)
	
	@Transactional(readOnly = false)
	fun create(dto: UserCreateDto): UserDto {
		val entity = userMapper.toEntity(dto).copy(id = UUID.randomUUID())
		val saved = userRepository.save(entity)
		return userMapper.toDto(saved)
	}
	
	@Transactional(readOnly = false)
	fun createPlayer(dto: PlayerCreateDto): UserDto {
		val user = userRepository.save(
			UserEntity(
				id = UUID.randomUUID(),
				name = dto.name,
				email = dto.email,
				bio = dto.bio
			)
		)
		
		// создаём профиль игрока, чтобы он сразу считался PLAYER
		playerProfileRepository.save(
			PlayerProfileEntity(
				user = user,
			)
		)
		
		return toUserDtoWithRoles(user)
	}
	
	@Transactional(readOnly = false)
	fun patch(id: UUID, dto: UserPatchDto): UserDto {
		val user = userRepository.findById(id)
			.orElseThrow { NotFoundException(USER_NOT_FOUND) }
		userMapper.updateEntityFromPatch(dto, user)
		val saved = userRepository.save(user)
		return userMapper.toDto(saved)
	}
	
	@Transactional(readOnly = false)
	fun delete(id: UUID) {
		if (!userRepository.existsById(id)) throw NotFoundException(USER_NOT_FOUND)
		userRepository.deleteById(id)
	}
	
	@Transactional(readOnly = false)
	fun addPlayerProfile(userId: UUID) {
		val user = userRepository.findById(userId)
			.orElseThrow { NotFoundException(USER_NOT_FOUND) }
		if (playerProfileRepository.existsByUser(user))
			throw AlreadyExistsException("Player profile already exists")
		playerProfileRepository.save(PlayerProfileEntity(user))
	}
	
	@Transactional(readOnly = false)
	fun removePlayerProfile(userId: UUID) {
		playerProfileRepository.deleteByUserId(userId)
	}
	
	@Transactional(readOnly = false)
	fun addDungeonMasterProfile(userId: UUID) {
		val user = userRepository.findById(userId)
			.orElseThrow { NotFoundException(USER_NOT_FOUND) }
		if (dungeonMasterProfileRepository.existsByUser(user))
			throw AlreadyExistsException("Dungeon Master profile already exists")
		dungeonMasterProfileRepository.save(DungeonMasterProfileEntity(user))
	}
	
	@Transactional(readOnly = false)
	fun removeDungeonMasterProfile(userId: UUID) {
		dungeonMasterProfileRepository.deleteByUserId(userId)
	}
	
	@Transactional(readOnly = false)
	fun addAdminProfile(userId: UUID) {
		val user = userRepository.findById(userId)
			.orElseThrow { NotFoundException(USER_NOT_FOUND) }
		if (adminProfileRepository.existsByUser(user))
			throw AlreadyExistsException("Admin profile already exists")
		adminProfileRepository.save(AdminProfileEntity(user))
	}
	
	@Transactional(readOnly = false)
	fun removeAdminProfile(userId: UUID) {
		adminProfileRepository.deleteByUserId(userId)
	}
}
