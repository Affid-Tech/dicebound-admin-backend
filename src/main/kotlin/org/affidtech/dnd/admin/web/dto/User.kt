package org.affidtech.dnd.admin.web.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.util.UUID


enum class UserRole { PLAYER, DUNGEON_MASTER, ADMIN }

// Для отдачи наружу
data class UserDto(
	val id: UUID,
	val name: String,
	val email: String?,
	val bio: String?,
	val roles: List<UserRole>
)

// Для создания нового пользователя (если нужно)
data class UserCreateDto(
	@field:NotBlank
	val name: String,
	val email: String?,
	val bio: String?
)

data class UserPatchDto(
	val name: String? = null,
	val email: String? = null,
	val bio: String? = null
)

data class PlayerCreateDto(
	@field:NotBlank
	val name: String,
	
	@field:Email
	val email: String? = null,
	
	val bio: String? = null,
)