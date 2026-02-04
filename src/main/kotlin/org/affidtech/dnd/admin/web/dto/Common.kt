package org.affidtech.dnd.admin.web.dto

import org.springframework.data.domain.Page

data class PageResponseDto<T>(
	val content: List<T>,
	val page: Int,
	val size: Int,
	val totalElements: Long,
	val totalPages: Int
)

fun <T: Any> Page<T>.toPageResponseDto(): PageResponseDto<T> =
	PageResponseDto(
		content = this.content,
		page = this.number,
		size = this.size,
		totalElements = this.totalElements,
		totalPages = this.totalPages
	)