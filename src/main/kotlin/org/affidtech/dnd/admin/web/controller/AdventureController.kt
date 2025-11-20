package org.affidtech.dnd.admin.web.controller

import org.affidtech.dnd.admin.domain.AdventureStatus
import org.affidtech.dnd.admin.domain.AdventureType
import org.affidtech.dnd.admin.service.AdventureService
import org.affidtech.dnd.admin.service.file.FileStorageService
import org.affidtech.dnd.admin.web.dto.AdventureCreateDto
import org.affidtech.dnd.admin.web.dto.AdventureDto
import org.affidtech.dnd.admin.web.dto.AdventurePatchDto
import org.affidtech.dnd.admin.web.dto.PageResponseDto
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.util.*

@RestController
@RequestMapping("/api/adventures")
class AdventureController(
	private val fileStorageService: FileStorageService,
	private val adventureService: AdventureService
) {
	
	@GetMapping
	fun search(
		pageable: Pageable,
		@RequestParam(required = false) statuses: List<AdventureStatus>?,
		@RequestParam(required = false) types: List<AdventureType>?,
		@RequestParam(required = false) dungeonMasterIds: List<UUID>?
	): PageResponseDto<AdventureDto> =
		adventureService.search(
			pageable = pageable,
			statuses = statuses,
			types = types,
			dungeonMasterIds = dungeonMasterIds
		)
	
	@GetMapping("/{id}")
	fun getById(@PathVariable id: UUID): AdventureDto = adventureService.getById(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody dto: AdventureCreateDto): AdventureDto =
		adventureService.create(dto)
	
	@PostMapping("/{id}/cover")
	fun uploadCover(
		@PathVariable id: UUID,
		@RequestParam("file") file: MultipartFile
	): Map<String, String> {
		val url = fileStorageService.saveCover(id, file)
		adventureService.updateCoverUrl(id, url)
		return mapOf("coverUrl" to url)
	}
	
	@PatchMapping("/{id}")
	fun patch(
		@PathVariable id: UUID,
		@RequestBody dto: AdventurePatchDto
	): AdventureDto = adventureService.patch(id, dto)
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: UUID) = adventureService.delete(id)
}
