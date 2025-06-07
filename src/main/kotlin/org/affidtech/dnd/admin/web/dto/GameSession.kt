package org.affidtech.dnd.admin.web.dto

import java.time.OffsetDateTime
import java.util.UUID

data class GameSessionDto(
	val id: UUID,
	val adventureId: UUID,
	val startTime: OffsetDateTime,
	val durationHours: Short,
	val linkFoundry: String?,
	val notes: String?
)

// Для создания новой сессии
data class GameSessionCreateDto(
	val adventureId: UUID,
	val startTime: OffsetDateTime,
	val durationHours: Short,
	val linkFoundry: String? = null,
	val notes: String? = null
)

data class GameSessionPatchDto(
	val startTime: OffsetDateTime? = null,
	val durationHours: Short? = null,
	val linkFoundry: String? = null,
	val notes: String? = null
)
