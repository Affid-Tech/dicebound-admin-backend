package org.affidtech.dnd.admin.web.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import org.affidtech.dnd.admin.domain.AdventureStatus
import org.affidtech.dnd.admin.domain.AdventureType
import java.util.*

data class AdventureDto(
	val id: UUID,
	val type: AdventureType,
	val status: AdventureStatus,
	val gameSystem: String,
	val title: String,
	val coverUrl: String?,
	val dungeonMaster: UserDto,
	val description: String?,
	val startLevel: Short?,
	val minPlayers: Short,
	val maxPlayers: Short,
	val priceUnits: Int?,
	val sessions: List<GameSessionDto> = listOf(),
	val signups: List<AdventureSignupDto> = listOf()
)

// Для создания приключения (request)
data class AdventureCreateDto(
	@field:NotNull val type: AdventureType,
	@field:NotNull val status: AdventureStatus,
	@field:NotBlank val gameSystem: String,
	@field:NotBlank val title: String,
	@field:NotNull val dungeonMasterId: UUID,
	val description: String? = null,
	@field:Positive val startLevel: Short? = null,
	@field:NotNull @field:Positive val minPlayers: Short,
	@field:NotNull @field:Positive val maxPlayers: Short,
	@field:Positive val priceUnits: Int? = null
)

data class AdventurePatchDto(
	val type: AdventureType? = null,
	val status: AdventureStatus? = null,
	val gameSystem: String? = null,
	val title: String? = null,
	val dungeonMasterId: UUID? = null,
	val description: String? = null,
	val startLevel: Short? = null,
	val minPlayers: Short? = null,
	val maxPlayers: Short? = null,
	val priceUnits: Int? = null
)
