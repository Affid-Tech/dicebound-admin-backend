package org.affidtech.dnd.admin.web.dto

import org.affidtech.dnd.admin.domain.AdventureSignupStatus
import java.util.UUID

data class AdventureSignupDto(
	val id: UUID,
	val adventureId: UUID,
	val user: UserDto,
	val status: AdventureSignupStatus
)

// Для создания заявки на приключение
data class AdventureSignupCreateDto(
	val adventureId: UUID,
	val userId: UUID
)

data class AdventureSignupPatchDto(
	val status: AdventureSignupStatus? = null
)
