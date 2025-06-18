package org.affidtech.dnd.admin.web.controller

import org.affidtech.dnd.admin.service.AdventureService
import org.affidtech.dnd.admin.service.file.FileStorageService
import org.affidtech.dnd.admin.web.dto.AdventureCreateDto
import org.affidtech.dnd.admin.web.dto.AdventureDto
import org.affidtech.dnd.admin.web.dto.AdventurePatchDto
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
	fun getAll(): List<AdventureDto> = adventureService.getAll()
	
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
