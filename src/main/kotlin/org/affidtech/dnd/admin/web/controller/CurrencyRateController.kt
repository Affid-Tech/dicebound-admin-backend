package org.affidtech.dnd.admin.web.controller

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import jakarta.validation.Valid
import org.affidtech.dnd.admin.service.CurrencyRateService
import org.affidtech.dnd.admin.web.dto.CurrencyRateCreateDto
import org.affidtech.dnd.admin.web.dto.CurrencyRateDto
import org.affidtech.dnd.admin.web.dto.CurrencyRatePatchDto

@RestController
@RequestMapping("/api/currency")
class CurrencyRateController(
	private val currencyRateService: CurrencyRateService
) {
	@GetMapping
	fun getAll(): List<CurrencyRateDto> = currencyRateService.getAll()
	
	@GetMapping("/{currency}")
	fun getByCurrency(@PathVariable currency: String): CurrencyRateDto =
		currencyRateService.getByCurrency(currency.uppercase())
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	fun create(@RequestBody @Valid dto: CurrencyRateCreateDto): CurrencyRateDto =
		currencyRateService.create(dto.copy(currency = dto.currency.uppercase()))
	
	@PatchMapping("/{currency}")
	fun patch(
		@PathVariable currency: String,
		@RequestBody dto: CurrencyRatePatchDto
	): CurrencyRateDto = currencyRateService.patch(currency.uppercase(), dto)
	
	@DeleteMapping("/{currency}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	fun delete(@PathVariable currency: String) = currencyRateService.delete(currency.uppercase())
}
