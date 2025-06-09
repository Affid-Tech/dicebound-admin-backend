package org.affidtech.dnd.admin.web.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import java.util.*
import jakarta.validation.Valid
import org.affidtech.dnd.admin.service.AdventureSignupService
import org.affidtech.dnd.admin.web.dto.AdventureSignupCreateDto
import org.affidtech.dnd.admin.web.dto.AdventureSignupDto
import org.affidtech.dnd.admin.web.dto.AdventureSignupPatchDto

@RestController
@RequestMapping("/api/signups")
class AdventureSignupController(
	private val adventureSignupService: AdventureSignupService
) {
	@GetMapping
	fun getAll(): List<AdventureSignupDto> = adventureSignupService.getAll()
	
	@GetMapping("/{id}")
	fun getById(@PathVariable id: UUID): AdventureSignupDto = adventureSignupService.getById(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody @Valid dto: AdventureSignupCreateDto): AdventureSignupDto =
		adventureSignupService.create(dto)
	
	@PatchMapping("/{id}")
	fun patch(@PathVariable id: UUID, @RequestBody dto: AdventureSignupPatchDto): AdventureSignupDto =
		adventureSignupService.patch(id, dto)
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: UUID) = adventureSignupService.delete(id)
	
}