package org.affidtech.dnd.admin.web.controller

import jakarta.validation.Valid
import org.affidtech.dnd.admin.service.UserService
import org.affidtech.dnd.admin.web.dto.PageResponseDto
import org.affidtech.dnd.admin.web.dto.PlayerCreateDto
import org.affidtech.dnd.admin.web.dto.UserCreateDto
import org.affidtech.dnd.admin.web.dto.UserDto
import org.affidtech.dnd.admin.web.dto.UserPatchDto
import org.affidtech.dnd.admin.web.dto.UserRole
import org.springframework.data.domain.Pageable
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("/api/users")
@Validated
class UserController(
	private val userService: UserService
) {
	
	@GetMapping
	fun search(
		pageable: Pageable,
		@RequestParam(required = false) role: UserRole?,
		@RequestParam(required = false, name = "q") keyword: String?
	): PageResponseDto<UserDto> =
		userService.search(pageable, role, keyword)
	
	@GetMapping("/{id}")
	fun getById(@PathVariable id: UUID): UserDto = userService.getById(id)
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody @Valid dto: UserCreateDto): UserDto =
		userService.create(dto)
	
	@PostMapping("/players")
	@ResponseStatus(HttpStatus.CREATED)
	fun createPlayer(@RequestBody @Valid dto: PlayerCreateDto): UserDto =
		userService.createPlayer(dto)
	
	@PatchMapping("/{id}")
	fun patch(
		@PathVariable id: UUID,
		@RequestBody dto: UserPatchDto
	): UserDto = userService.patch(id, dto)
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable id: UUID) = userService.delete(id)
	
}
