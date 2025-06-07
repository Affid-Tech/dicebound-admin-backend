package org.affidtech.dnd.admin.web.dto

import java.time.OffsetDateTime

data class CurrencyRateDto(
	val currency: String,
	val ratio: Int,
	val updatedAt: OffsetDateTime
)

data class CurrencyRateCreateDto(
	val currency: String,
	val ratio: Int
)

data class CurrencyRatePatchDto(
	val ratio: Int? = null,
)
