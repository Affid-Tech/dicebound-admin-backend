package org.affidtech.dnd.admin.web.dto

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
	val name: String,
	val email: String?,
	val bio: String?
)

data class UserPatchDto(
	val name: String? = null,
	val email: String? = null,
	val bio: String? = null
)
